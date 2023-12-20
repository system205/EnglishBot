# English Bot

## 📔 Features

- Show saved words
- Add words
- Scheduled notification to a user with some word from the vocabulary
- Get info about user settings (interval, last notification, number of words, number of daily words)
- Education plan - tracks daily words, updatable
- Set number of daily words

## ⚙ Deploy

1. Create ```.env``` file with the following environment variables:

   - DB_USERNAME and POSTGRES_USER should be equal 
   - DB_PASSWORD and POSTGRES_PASSWORD should be equal
   - OWNER_ID - your telegram id
   - BOT_TOKEN - bot API token obtained from [BotFather](https://t.me/BotFather)

2. ```./mvnw clean install -DskipTests```
3. ```docker build -t system205/english-bot:latest```
4. ```docker-compose up```
5. Alternatively with Maven plugin:
   1. Run ```./mvnw -DskipTests=true spring-boot:build-image```
   2. Set up ```DOCKER_LOGIN``` and ```DOCKER_PASSWORD``` as env. variables
   3. So, you can specify ```-Dspring-boot.build-image.publish=true``` to publish the image

## 🧾 [Properties](./src/main/resources/application.yaml)

- **bot.scheduling.rate** in milliseconds - how often to check for available notifications to be sent
- **bot.scheduling.default-interval** in minutes - how often to send notification to a user 

