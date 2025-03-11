# Expense Tracker Backend

### Jasypt Configuration
Set Environment Variable: 
```
JASYPT_ENCRYPTOR_PASSWORD = secretKey
```
Encrypt your environment value using this command:
```bash
mvn jasypt:encrypt-value -Djasypt.encryptor.password="secretKey" -Djasypt.plugin.value="SuperSecret"
```

### Dockerization
The service uses [Google Jib](https://cloud.google.com/java/getting-started/jib) for packaging

To build an image for at **local workstation**:
```bash
mvn clean verify -DskipTests -U jib:dockerBuild 
```
- ⚠️ Docker engine required to be working on localhost
- ⚠️ You need to set proxy on your maven setting to bypass sanctions from Iran 
- ⚠️ For this reason, I forced maven plugin to use local base image. Now you just need to pull base image first:
```bash
docker pull alpine/java:21-jdk  
```