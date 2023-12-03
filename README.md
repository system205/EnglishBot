# English Bot

## 📔 Features

- Show saved words
- Add words
- Notify a user with a random word

## ⚙ Deploy

1. Create ```.env``` file with the following environment variables:

   - DB_USERNAME and POSTGRES_USER should be equal 
   - DB_PASSWORD and POSTGRES_PASSWORD should be equal
   - OWNER_ID - your telegram id
   - BOT_TOKEN - bot API token obtained from [BotFather](https://t.me/BotFather)

2. ```./mvnw clean install -DskipTests```
3. ```docker build -t system205/english-bot:latest```
4. ```docker-compose up```

## 🧾 [Properties](./src/main/resources/application.yaml)

- **bot.scheduling.rate** in milliseconds - how often to check for available notifications to be sent
- **bot.scheduling.default-interval** in minutes - how often to send notification to a user 

