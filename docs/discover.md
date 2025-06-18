<script setup>
import { ref } from 'vue'
import DiscordMessage from './.vitepress/components/DiscordMessage.vue'
</script>

# Discover

Sunny is a smart, friendly support helper bot for Discord.

<DiscordMessage authorUrl=./images/cat.jpg authorName="Bog" messageContent="What is the IP of the server?" />
<br>
<DiscordMessage authorUrl=./images/sunny.webp authorName="Sunny" messageContent="Hi, Bog. I found something that might help you. If not, a knowledgeable human will come to assist!
" embedTitle="What version is the server on? / What is the server's address?" embedDescription="The server is running on Minecraft: Java Edition 1.21.4. Connect to it using either woftnw.org, play.woftnw.org, or woftnw.duckdns.org." />

## Why a support helper?

**For users who need support:**

- **Instant answers, anytime:** Sunny gives users a quick first response, so they’re never left waiting.
- **Friendly and approachable:** Sunny sets a warm tone, making your support channel feel more inviting.
- **Helpful suggestions:** It finds and shares relevant articles, guides, and documentation to point users in the right direction.

**For your project and support team:**

- **Reduces repetitive questions:** Sunny automatically replies to common inquiries, saving time and energy.
- **Promotes self-service:** By guiding users to documentation, Sunny encourages independent problem-solving.
- **Keeps your team focused:** With fewer interruptions, your team can concentrate on issues and improvements.

<DiscordMessage authorUrl=./images/sunny.webp authorName="Sunny" messageContent="Hey, nice to meet you! I’m Sunny, and I’m here to help you find answers. I’ll give every message a shot, but if I can’t find anything right away, just @mention me and I’ll try again with the best match I’ve got. Welcome to Wings of Fire: The New World!" />

## Why Sunny Bot?

Sunny stores potential answers in an index. Each answer has an associated query reference, which contains likely questions for that answer. You can have multiple indexes and link each to multiple channels. For example, you can have an API answer index for your **#api-support** channel and a rendering answer index for **#rendering-support**.

When a user asks a quesion, Sunny uses cosine similarity to compare the user's question to each answer's query reference. The answers are ranked by similarity and the top one, if it exceeds the minimum threshold, is presented to the user.

The user or someone who is knowledgeable can optionally confirm that the reponse was the correct answer or collapse it if it was not.