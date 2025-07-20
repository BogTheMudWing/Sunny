# Sunny

A smart, friendly support helper Discord bot.

- [Invite to Server](https://discord.com/oauth2/authorize?client_id=1384182700285493380)
- [Get Started](https://sunny.macver.org/start)
- [Discover More](https://sunny.macver.org/discover)

## About

Sunny is a Discord bot written in Java that automatically responds to user queries. It is designed first to answer questions and deliver support, but it can be used for general-purpose auto-replies too.

### Why a support helper?

**For users who need support:**

- **Instant answers, anytime:** Sunny gives users a quick first response, so they’re never left waiting.
- **Friendly and approachable:** Sunny sets a warm tone, making your support channel feel more inviting.
- **Helpful suggestions:** It finds and shares relevant articles, guides, and documentation to point users in the right direction.

**For your project and support team:**

- **Reduces repetitive questions:** Sunny automatically replies to common inquiries, saving time and energy.
- **Promotes self-service:** By guiding users to documentation, Sunny encourages independent problem-solving.
- **Keeps your team focused:** With fewer interruptions, your team can concentrate on issues and improvements.

### Why Sunny Bot?

Sunny stores potential answers in an index. Each answer has an associated query reference, which contains likely questions for that answer. You can have multiple indexes and link each to multiple channels. For example, you can have an API answer index for your #api-support channel and a rendering answer index for #rendering-support.

When a user asks a quesion, Sunny uses cosine similarity to compare the user's question to each answer's query reference. The answers are ranked by similarity and the top one, if it exceeds the minimum threshold, is presented to the user.

The user or someone who is knowledgeable can optionally select whether the reponse was the correct answer or collapse it if it was not. Sunny will use that feedback to tune the query reference for that answer and deliver more accurate responses in the future.

## Use

See the documentation at https://sunny.macver.org/start.

## Development

To build the project from source, simply run the command `./gradlew shadowJar`. The completed jar will appear in `build/libs/Sunny-v*.jar`.

## Contributing

There are many ways in which you can contribute:

- For bug reports, please create an issue on the GitHub repository detailing exactly how the error occurred and attaching any relevant crash reports or logs.
- For feature requests, please create an issue on the GitHub repository detailing what the feature or enhancement should be and how it would improve the project.
- For pull requests, please maintain good coding practices and take feedback eagerly.
- You can also contribute simply by sharing the bot with other people who might like it :)

## Thanks to

- Discord for their chat platform and developer API.
- JDA for making a great library for Discord API.
- GitHub, for hosting my code.
- Apache, for the cosine similarity search system.
- IntelliJ, for developing a great IDE.
- Jackson, for the JSON serialization library.
- LanguageTool, for spell checking.
- VitePress, for the awesome documentation site.

---

[![Bog The MudWing](https://blog.macver.org/content/images/2025/07/Stamp-Colored-Small-Shadow.png)](https://blog.macver.org/about-me)
