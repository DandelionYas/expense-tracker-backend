## Jasypt Configuration
Set Environment Variable: JASYPT_ENCRYPTOR_PASSWORD = secretKey
mvn jasypt:encrypt-value -Djasypt.encryptor.password="secretKey" -Djasypt.plugin.value="SuperSecret"