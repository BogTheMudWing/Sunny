<script setup>
import { ref } from 'vue'
import DiscordMessage from './.vitepress/components/DiscordMessage.vue'
</script>

# Start

## Install

Start by adding the bot to your server using the authorization link.

https://discord.com/oauth2/authorize?client_id=1384182700285493380

Sunny needs the following permissions:

- **View Channels** to watch for user questions.
- **Send Messages** to reply with answers.
- **Attach Files** to export your guild data as a JSON file.

## Setup

Now it's time to set up your first index! Follow along with the example or adapt to your own needs.

To start, create the index. The name of the index can be whatever you want. It's for your reference only, so users won't see this. I'll name mine *Server* to indicate that it contains answers about my server.

```
/index create name:Server
```

Great! Now I have an index called *Server*, but it doesn't have any answers yet. Let's create one now. I get a lot of questions from members who want to become moderators, so I'll make an answer for that.

```
/index answer add index:Server title:Joining the Staff Team content:We aren't looking for new staff members right now. We open applications from time to time, so keep an eye on the announcements channel.
```

Now I've created an answer in my *Server* index that looks like this:

> **Joining the Staff Team**
>
> We aren't looking for new staff members right now. We open applications from time to time, so keep an eye on the announcements channel.

If I want to add a URL, I can edit the answer like this:

```
/index answer edit index:Server title:Joining the Staff Team new_url:https://docs.woftnw.org/books/admin-handbook/chapter/staff-positions
```

Now, users can follow the link to read more about joining the staff team.

> [**Joining the Staff Team**](https://docs.woftnw.org/books/admin-handbook/chapter/staff-positions)
>
> We aren't looking for new staff members right now. We open applications from time to time, so keep an eye on the announcements channel.
>
> [`Open resource`](https://docs.woftnw.org/books/admin-handbook/chapter/staff-positions)

Perfect. Now I need to set the query reference so that Sunny knows what kinds of questions to look for. I have a list of potential questions that this answer is helpful for.

- *How do I become staff?*
- *Can I be a mod?*
- *When are helper applications?*
- *Can I have admin?*
- *I want to join the staff.*
- *Are staff apps open?*

This should be a good starting list. Any questions that are similar to these will be matched by Sunny, so I don't need to add every possible question. If someone asks a relevant question that doesn't trigger the answer, I can add it later to improve Sunny's matching.

Sunny also tries to autocorrect user queries before matching them, so don't include mispelled words in the query reference.

Now I'll add the potential questions to the answer's query reference. Add them one after another, separated by semicolons.

```
/index answer edit index:Server title:Joining the Staff Team new_query:How do I become staff?;Can I be a mod?;When are helper applications?;Can I have admin?;I want to join the staff.;Are staff apps open?
```

Perfect. The final step is to add a channel for the index. I have a **#server-questions** channel that I'll add it to.

```
/index channel link channel:#server-questions index:Joining the Staff Team
```

You can add as many channels as you want. You can even have multiple indexes linked to a channel.

Now, when a message is sent to that channel, Sunny will check if it's a question similar to the ones in the query reference. If it matches, Sunny will suggest the answer. Hooray!

<DiscordMessage authorUrl=./images/cat.jpg authorName="Bog" messageContent="Can I be a helper?" />
<br>
<DiscordMessage authorUrl=./images/sunny.webp authorName="Sunny" messageContent="Hey, Bog! I found something that might be useful. If it’s not quite right, don’t worry—a helpful human will be with you soon!" embedTitle="Joining the Staff Team" embedDescription="We aren't looking for new staff members right now. We open applications from time to time, so keep an eye on the announcements channel." />