---
# https://vitepress.dev/reference/default-theme-home-page
layout: home

hero:
  name: Sunny
  text: "Smart, friendly support helper"
  tagline: "Automatically answer common questions and guide users to documentation"
  image:
    src: "/images/Sunny Transparent.png"
    alt: "Sunny"
  actions:
    - theme: brand
      text: Invite to Server
      link: https://discord.com/oauth2/authorize?client_id=1384182700285493380
    - theme: alt
      text: Get Started
      link: /start
    - theme: alt
      text: Discover More
      link: /discover

features:
  - icon: 🧲
    title: Smart cosine-similarity searching 
    details: Sunny intelligently matches what the user is asking for based on a list of common questions.
  - icon: 🗃
    title: Channel-independent answer indexes
    details: You can have multiple sets of answers for different support channels.
  - icon: 👋
    title: Friendly by default
    details: Sunny has multiple variants for each friendly response to make users feel welcome.
---

<script setup>
import { ref } from 'vue'
import DiscordMessage from './.vitepress/components/DiscordMessage.vue'
</script>

<br>

**Sunny is designed to make your support channels better for everyone.**

I see a lot of Discord support threads that look something like this:

<br>
<DiscordMessage authorUrl=./images/pexels-pixabay-86591.webp authorName="ArrowSparrow" messageContent="What is the IP of the server?" />
<br>
<DiscordMessage authorUrl=./images/cube.webp authorName="[Mod] Hyla" messageContent="Check FAQ" />
<br>
<DiscordMessage authorUrl=./images/pexels-goumbik-440122.webp authorName="Toriberry" messageContent="Can anyone help me?" />
<br>
<DiscordMessage authorUrl=./images/cube.webp authorName="[Mod] Hyla" messageContent="What do you need?" />
<br>
<DiscordMessage authorUrl=./images/pexels-goumbik-440122.webp authorName="Toriberry" messageContent="When I tried to extract the download, it gave me an error" />
<br>
<DiscordMessage authorUrl=./images/cube.webp authorName="[Mod] Hyla" messageContent="I'm not sure. Maybe try downloading it again?" />
<br>

Support members are frustrated when users don't read documentation or don't ask useful questions. Users can feel ignored, disregarded, or antagonized by support channels.

With Sunny, you can transform your support channel into something this:

<br>

<DiscordMessage authorUrl=./images/pexels-pixabay-86591.webp authorName="ArrowSparrow" messageContent="What is the IP of the server?" />
<br>
<DiscordMessage authorUrl=./images/sunny.webp authorName="Sunny" messageContent="Hello there, ArrowSparrow! I pulled up something that might work for you. If it misses the mark, a human expert will be here shortly to help out!" embedTitle="What version is the server on? / What is the server's address?" embedDescription="The server is running on Minecraft: Java Edition 1.21.4. Connect to it using either woftnw.org, play.woftnw.org, or woftnw.duckdns.org." />
<br>
<DiscordMessage authorUrl=./images/pexels-goumbik-440122.webp authorName="Toriberry" messageContent="Can anyone help me?" />
<br>
<DiscordMessage authorUrl=./images/sunny.webp authorName="Sunny" messageContent="Hi, Toriberry. Here’s something I think could help. If it’s not a perfect fit, a knowledgeable human will assist you shortly!" embedTitle="Don't ask to ask. Just ask." embedDescription="Don't ask 'Any Java experts around?', but rather ask 'How do I do [problem] with Java and [other relevant info]?'" />
<br>
<DiscordMessage authorUrl=./images/pexels-goumbik-440122.webp authorName="Toriberry" messageContent="When I tried to extract the download, it gave me an error. How do I fix it?" />
<br>
<DiscordMessage authorUrl=./images/cat.jpg authorName="[Admin] Bog" messageContent="I can help you troubleshoot. Can you send a screenshot of the error?" />
<br>

---

[![Bog The MudWing](https://nextcloud.macver.org/apps/files_sharing/publicpreview/jyWLnm4i724mxXg?file=/&fileId=61792&x=3390&y=1906&a=true&etag=c43260166526abc326861afd5244df8e)](https://blog.macver.org/about-me)