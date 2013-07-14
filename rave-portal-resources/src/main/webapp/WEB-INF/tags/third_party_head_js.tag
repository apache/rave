<%--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.

--%>
<%--
     This file includes references to all external third party javascript libraries required by rave
     that must be loaded in the <HEAD> tag.

     Overlay this file in custom extensions of Rave if you host these libraries internally and don't need
     to pull them in externally.

     NOTE: use this file sparingly and only for libraries that absolutely have to
           be loaded in the <HEAD>.  For improved performance all other JavaScript
           libraries should be loaded from third_party_js.tag
--%>
<!-- Modernizr MUST come first!-->
<script src="//cdnjs.cloudflare.com/ajax/libs/modernizr/2.5.3/modernizr.min.js"></script>
<%-- require.js --%>
<script src="//cdnjs.cloudflare.com/ajax/libs/require.js/2.1.5/require.js"></script>
