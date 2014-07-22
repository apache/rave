# -*- mode: ruby -*-
# vi: set ft=ruby :

# Pathing variables
vagrant_dir = File.expand_path(File.dirname(__FILE__))

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.vm.provider :virtualbox do |v|
    v.customize ["modifyvm", :id, "--memory", 1024]
  end

  config.ssh.forward_agent = true
  config.vm.hostname = "rave.dev"
  config.vm.network :private_network, ip: "192.168.13.37"
  config.vm.box = "hashicorp/precise64"

  # load the project into the Vagrant
  config.vm.synced_folder vagrant_dir, "/rave/", :mount_options => [ "dmode=775", "fmode=774" ]

  if defined? VagrantPlugins::HostsUpdater
    config.hostsupdater.aliases = ["rave.dev"]
  end

  # Provision
  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "deploy/ansible/rave-local-playbook.yml"
  end

end
