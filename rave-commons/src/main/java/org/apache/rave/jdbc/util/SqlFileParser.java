/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.jdbc.util;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringTokenizer;

/**
 * Parses a file looking for create, alter, insert, update, delete or drop commands and appends them to an output
 * string or follows @@ syntax to read child scripts.
 */
public class SqlFileParser {
    private static final Pattern WORD_PATTERN = Pattern.compile("^([a-zA-Z]*)[ ;]");
    private static final String CHILD_SCRIPT_INDICATOR = "@@";
    private static final Set<String> commandSet;

    static {
        commandSet = new HashSet<String>();
        commandSet.add("create");
        commandSet.add("alter");
        commandSet.add("insert");
        commandSet.add("update");
        commandSet.add("drop");
        commandSet.add("delete");
        commandSet.add("commit");
        commandSet.add("set");
        commandSet.add("truncate");
        commandSet.add("rollback");
    }

    private enum State {
        INIT,
        READFILE,
        READSQL
    }

    private Stack<State> stateStack;
    private Resource resource;

	 private PasswordEncoder passwordEncoder;

    /**
     * Constructor takes a Spring {@link org.springframework.core.io.Resource}
     *
     * @param resource the initial file to parse
     */

    public SqlFileParser(Resource resource) {
        stateStack = new Stack<State>();
        this.resource = resource;

		  passwordEncoder=new ShaPasswordEncoder();
    }

    /**
     * Gets the executable SQL statements from the resource passed to the constructor and its children
     *
     * @return a valid executable string containing SQL statements
     * @throws java.io.IOException if the resource or its children are not found
     */
    public String getSQL() throws IOException {
        return processResource(resource);
    }

    private String processResource(Resource res) throws IOException {
        StringBuilder sql = new StringBuilder();
        File resourceFile = res.getFile();
        stateStack.push(State.INIT);
        processFile(resourceFile, sql);
        stateStack.pop();
        return sql.toString();
    }

    private void processFile(File file, StringBuilder sql) throws IOException {
		  BufferedReader fileReader=null;
		  try {
				fileReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
				String line = null;
				while ((line = fileReader.readLine()) != null) {
					 processLine(sql, line);
				}
		  }
		  catch (IOException ioex) {
				throw (ioex);
		  }
		  finally {
				if(fileReader != null) {
                    fileReader.close();
                }
		  }
    }

    private void processLine(StringBuilder sql, String line) throws IOException {
        String lowerLine = line.toLowerCase().trim();
        switch (stateStack.peek()) {
            case INIT: {
                if (lowerLine.startsWith(CHILD_SCRIPT_INDICATOR)) {
                    //replace the current element in the stack with the new state
                    stateStack.pop();
                    stateStack.push(State.READFILE);
                    processLine(sql, line);
                } else if (commandSet.contains(getFirstWord(lowerLine))) {

                    //replace the current element in the stack with the new state
                    stateStack.pop();
                    stateStack.push(State.READSQL);
                    processLine(sql, line);
                }
                break;
            }
            case READFILE: {
                stateStack.push(State.INIT);
                Resource child = resource.createRelative(line.replace(CHILD_SCRIPT_INDICATOR, ""));
                sql.append(processResource(child));
                //Read File lines do not have a terminal character but are by definition only one line long
                stateStack.pop();
                stateStack.push(State.INIT);
                break;
            }
            case READSQL: {
					 //This is specific to Rave's initial_data.sql.
					 //TODO replace this with an external, pluggable utility class.
					 line=hashAndSaltPassword(line);

                sql.append(line);
                //add a space to accommodate line breaks.  Not a big deal if extraneous spaces are added
                sql.append(" ");
                if (lowerLine.endsWith(";")) {
                    stateStack.pop();
                    stateStack.push(State.INIT);
                }
                break;
            }
            default: {
                throw new RuntimeException("Invalid State");
            }
        }
    }
	 
	 //TODO: this is specific to initial_data.sql while rest of the class code is 
	 //general purpose.  Need to find a better way to do this.
	 private String hashAndSaltPassword(String line) {
		  String newLine=line;

		  //TODO This will BREAK if the SQL line ever gets changed.
		  //TODO This is a not very good way to make sure we have the correct line.
		  if(line.indexOf("@user_id_")>-1 && line.indexOf("user_id_seq")>-1) {
				StringTokenizer st=new StringTokenizer(newLine,",");
				if(st.countTokens()>4) {
					 String userid=st.nextToken();
					 String userseq=st.nextToken();
					 String username=st.nextToken();
					 String password=st.nextToken();
					 username=stripQuotes(username);
					 password=stripQuotes(password);
					 //TODO: This assumes that the user name is used for the salt. This may change.
					 //See DefaultNewAccountService
					 String saltedHash=passwordEncoder.encodePassword(password,username);
					 newLine=replacePassword(newLine,password,saltedHash);
				}
				else {
					 //Line was unexpectedly formatted
				}
		  }
		  else {
				//Do nothing.
		  }
		  
		  return newLine;
	 }
	 
	 //Used to strip the single quotes around the input string
	 private String stripQuotes(String quotedString) {
		  StringBuilder unquoted=new StringBuilder(quotedString);
		  int index1=unquoted.indexOf("'");
		  int index2=unquoted.lastIndexOf("'");
		  return unquoted.substring(index1+1,index2);
	 }

	 //Replace the password in the original string with the hashed and salted password
	 private String replacePassword(String line, String password, String hashedPassword) {
		  StringBuilder newLine=new StringBuilder(line);
		  newLine=newLine.replace(line.lastIndexOf(password),line.lastIndexOf(password)+password.length(),hashedPassword);
		  return newLine.toString();
	 }

    private static String getFirstWord(String line) {
        Matcher match = WORD_PATTERN.matcher(line);
        return match.find() ? match.group(1) : null;
    }

}
