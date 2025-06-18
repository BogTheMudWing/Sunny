---
outline: deep
---

# Self-Host

Prefer to run Sunny on your own terms? You can self-host a private instance of the bot instead of using the public version.

## Why self-host?

- **Full control over your data:** Everything stays on your servers — no third-party storage.
- **Custom branding:** Personalize Sunny’s name, avatar, banner, and profile to match your community.
- **Upgrade on your schedule:** You choose when (or if) to apply updates — no surprises.

## Things to consider

- **Setup required:** You’ll need to install, configure, and maintain the bot yourself.
- **Hosting is on you:** Sunny needs a server or reliable hardware to stay online.
- **Manual updates:** New features and fixes won’t apply automatically — you’ll need to pull and deploy them yourself.

## How to get up and running

You'll need to prepare the following:

- A Discord account to create the bot with.
- Java or Docker on the server you want to host with.
- About fifteen minutes.

### Discord Setup

To begin, you'll need to create a bot. Go to https://discord.com/app and make sure that you're logged in. Once you've confirmed that, go to the applications page:

https://discord.com/developers/applications

Click the **New Application** button in the top right. Choose a name for your bot (it can be changed later) and agree to the Discord Developer Terms of Service and Developer Policy.

Open the new application. On the **General Information** tab, you can customize the description. The icon and name here do *not* change the bot.

Go to the **Bot** tab. Here you can customize the icon, banner, and username.

If you want to ensure that no one else can add the bot to servers, disable the **Public Bot** toggle.

::: tip
If you're having trouble finding something, you can usually use the keybind `Ctrl + F` to search for text on a page.
:::

To make sure your bot can read messages, enable the **Message Content Intent** toggle.

Now, go to the **Installation** tab and make sure **Guild Install** is enabled. At the bottom, there is a multiselect field labeled **Permissions**. Add Attach Files, Send Messages, and View Channels.

Finally, make sure **Install Link** is set to "Discord Provided Link" and copy and paste the link into your address bar to invite the bot to your server.

### Application Setup

There are two methods of installation.

[**Standalone**](#standalone) installation requries Java 21. If you aren't familiar with Docker, go this route.

[**Docker**](#docker) installation requires Docker. Docker allows for improved management and upgrades.

#### Standalone

You need to have Java 21 installed.

**On Debian-based Linux**, run `sudo apt update && sudo apt install openjdk-21-jre`.

**On Windows or macOS**, download Temurin OpenJDK 21 from [here](https://adoptium.net/temurin/releases/?os=any&arch=any&version=21) and open it to install. Follow the directions in the install wizard.

Download the application from GitHub to a convenient location. It is best to put it in its own folder. The file you want with `.jar`.

https://github.com/BogTheMudWing/releases

In the same folder as the application, create a file called `config.json` and insert this text:

```json
{
    "botToken": "TOKEN_HERE",
    "sendReportsTo": "505833634134228992"
}
```

::: info
`sendReportsTo` defines a Discord user to DM error reports to. In the example above, it is set to send error reports to me, but you can change it or remove it entirely if you wish.

Error reports contain no identifiable data and are used only for the purpose of fixing issues with Sunny.
:::

Don't close it yet. We're not done.

Go back to the [Discord applications](https://discord.com/developers/applications) page, open your application, and go to the **Bot** tab.

Click the **Reset Token** button and copy the token. It's a long string of random characters. Treat this like a password. Never share this token with anyone.

In the `config.json` file, replace `TOKEN_HERE` with the bot token you just copied. This will allow the application to connect to your bot and make it function. Save and close the file.

Congratulations! You're done with setup. Every time you want to start the bot, double click the `.jar` file to run it or open a terminal in the folder and run the command:

```bash
java -jar sunny-*.jar
```

If you experience any problems, open an issue report on GitHub at https://github.com/BogTheMudWing/Sunny/issues. Make sure you search for relevant issues before you create a new one.


#### Docker

I am going to assume you already have Docker installed.

Right-click the link below and **Save Link As** `compose.yml` to a convenient location.

https://raw.githubusercontent.com/BogTheMudWing/Sunny/refs/heads/main/compose.yml

In the same folder as the `compose.yml` file, create a file called `config.json` and insert this text:

```json
{
    "botToken": "TOKEN_HERE",
    "sendReportsTo": "505833634134228992"
}
```

::: info
`sendReportsTo` defines a Discord user to DM error reports to. In the example above, it is set to send error reports to me, but you can change it or remove it entirely if you wish.

Error reports contain no identifiable data and are used only for the purpose of fixing issues with Sunny.
:::

Don't close it yet. We're not done.

Go back to the [Discord applications](https://discord.com/developers/applications) page, open your application, and go to the **Bot** tab.

Click the **Reset Token** button and copy the token. It's a long string of random characters. Treat this like a password. Never share this token with anyone.

In the `config.json` file, replace `TOKEN_HERE` with the bot token you just copied. This will allow the application to connect to your bot and make it function. Save and close the file.

To initialize the container, run the command:

```bash
docker compose up -d
```

If you get an error, you may need to add `sudo` before the command to elevate privileges.

Congratulations! You're done with setup. Every time you want to start the bot, run this command:

```bash
docker container start sunny
```

If you experience any problems, open an issue report on GitHub at https://github.com/BogTheMudWing/Sunny/issues. Make sure you search for relevant issues before you create a new one.
