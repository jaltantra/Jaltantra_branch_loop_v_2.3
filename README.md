# Initial setup and run script
Please ensure you are connected to IIT-B network and have access to license server , then run the below scripts in order  
-bash inital_setup.sh  
-bash run.sh


# Project Deployment and Monitoring Guide


## Configuration Files

There are two configuration files you need to fill out: `deploy_config.json` and `monitor_config.json`.

### deploy_config.json

This configuration file contains parameters required for deploying your application.

#### Parameters

- **host_name**: The name of the host to which the deployment will be made.
  - **Example**: `"deploy"`

- **remote_ip**: The IP address of the remote server where the deployment will occur.
  - **Example**: `"10.192.6.131"`

- **remote_folder**: The directory path on the remote server where the Python scripts will be stored.
  - **Example**: `"/home/deploy/Jaltantra_v2_3_0_0"`

#### Example Configuration

```json
{
  "host_name": "deploy",
  "remote_ip": "10.192.6.131",
  "remote_folder": "/home/deploy/Jaltantra_v2_3_0_0"
}
```

# Monitoring Configuration Guide

This section details the configuration parameters for setting up monitoring of your application using `monitor_config.json`.

## Configuration Parameters

### host_ip

- **Description**: The IP address of the host server where your application is deployed.
- **Example**: `"localhost"`

### host_port

- **Description**: The port number on which the host server listens for incoming requests.
- **Example**: `"8099"`

### application_context

- **Description**: The context path of your application.
- **Example**: `"jaltantra_loop_dev_v7"`

### solver_directory

- **Description**: The directory path on the remote server where the Python scripts are stored.
- **Example**: `"/home/hkshenoy/Desktop/Jaltantra_loop/JalTantra-Code-and-Scripts/NetworkResults/"`

### license_directory
- **Description**: The directory path on the remote server which contains the AMPL license.
- **Example**: `"/home/deploy/Jaltantra_v2_3_0_0/JalTantra-Code-and-Scripts/ampl.linux-intel64"`

### sender_email

- **Description**: The email address used for sending notifications.
- **Example**: `"24m709@iitb.ac.in"`

### sender_token

- **Description**: The authentication token for the sender's email account.
- **Example**: `"6a853780b90d30aac01a7dummy6d58b36a06"`

### receiver_email_list

- **Description**: A list of email addresses that will receive monitoring notifications.
- **Example**: `["22m0759@iitb.ac.in", "22m0796@iitb.ac.in"]`

## Example Configuration

Here is an example `monitor_config.json` file with sample values filled in:

```json
{
  "host_ip": "localhost",
  "host_port": "8099",
  "application_context": "jaltantra_loop_dev_v7",
  "solver_directory": "/home/hkshenoy/Desktop/Jaltantra_loop/JalTantra-Code-and-Scripts/NetworkResults/",
  "license_directory":"/home/deploy/Jaltantra_v2_3_0_0/JalTantra-Code-and-Scripts/ampl.linux-intel64",
  "sender_email": "24m709@iitb.ac.in",
  "sender_token": "6a853780b90d30aac01a7dummy6d58b36a06",
  "receiver_email_list": ["22m0759@iitb.ac.in", "22m0796@iitb.ac.in"]
}
```


After configuring `monitor_config.json` with the appropriate values, execute the monitoring script using the following command:

```sh
bash monitor_app.sh monitor_config.json
```

# Documentation
- Refer Documentation.zip for Java class documentation

# Google MAP API KEY
- In the file src/main/resources/static/system.html please add your google map api key at line 51:
- <script type="text/javascript" src="//maps.google.com/maps/api/js?key=Add_KEY_HERE&libraries=geometry,places"></script>
- Please keep the API key secret and dont expose it to public/external users.

# Properties file
-Refer application-dev.properties and application-deploy.properties file fin src/main/resources folder for making changes to configurations
