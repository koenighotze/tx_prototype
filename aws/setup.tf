# Terraform setup for applications

# one EC2 for microservices
#
# one DB?
#
# one Consul?
#
# Messaging?
#
# Auth0?
#
#
provider "aws" {
  region = "eu-central-1"
}

resource "aws_instance" "microservices" {
  ami = "ami-3f1bd150"
  instance_type = "t2.micro"

  tags {
    Name = "tx-prototype"
    Owner = "dschmitz"
  }
}

resource "aws_instance" "database" {
  ami = "ami-3f1bd150"
  instance_type = "t2.micro"

  tags {
    Name = "tx-prototype"
    Owner = "dschmitz"
  }
}