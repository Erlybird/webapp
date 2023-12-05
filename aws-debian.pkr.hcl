packer {
  required_plugins {
    amazon = {
      source  = "github.com/hashicorp/amazon"
      version = ">= 1.0.0"
    }
  }
}


variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "jar_file" {
  type    = string
  default = ""
}

variable "source_ami" {
  type    = string
  default = "ami-06db4d78cb1d3bbf9" # Debian 12
}

variable "ssh_username" {
  type    = string
  default = "admin"
}

variable "subnet_id" {
  type    = string
  default = "subnet-0185a07b279bcab12"
}
variable "device_name" {
  type    = string
  default = "/dev/xvda"
}
#variable "volume_size" {
#  type    = integer
#  default = 8
#}
variable "volume_type" {
  type    = string
  default = "gp2"
}

# https://www.packer.io/plugins/builders/amazon/ebs
source "amazon-ebs" "my-ami" {
  region          = "${var.aws_region}"
  ami_name        = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for CSYE 6225"
  ami_users       = ["455958282906", "920403344186"]
  ami_regions = [
    "us-east-1"
  ]

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

  instance_type = "t2.micro"
  source_ami    = "${var.source_ami}"
  ssh_username  = "${var.ssh_username}"
  subnet_id     = "${var.subnet_id}"

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "${var.device_name}"
    volume_size           = 8
    volume_type           = "${var.volume_type}"
  }
}


build {
  sources = ["source.amazon-ebs.my-ami"]

  provisioner "file" {
    source      = "./opt/users.csv"
    destination = "/tmp/users.csv"
  }
  provisioner "file" {
    source      = "/home/runner/work/webapp/webapp/target/webapp-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/webapp-0.0.1-SNAPSHOT.jar"
  }

  provisioner "file" {
    source      = "./webapp.service"

    destination = "/tmp/webapp.service"
  }
  ##
  provisioner "shell" {
    inline = [
      "sudo mkdir -p /opt/csye6225",
      "sudo mv /tmp/webapp-0.0.1-SNAPSHOT.jar /opt/csye6225/webapp-0.0.1-SNAPSHOT.jar",
      "sudo mv /tmp/users.csv /opt/csye6225/users.csv",
      "sudo mv /tmp/webapp.service /opt/csye6225/webapp.service"

    ]
  }
  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1"
    ]
    script = "Java.sh"
  }

  provisioner "shell" {
    script = "service.sh"
  }


}