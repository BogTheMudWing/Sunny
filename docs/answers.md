# All About Answers

Each index contains a list of answers. Answers have five fields.

- **Title:** (internally `title`) the name of the answer. This is how the answer is uniquely identified and appears at the top of the embed when Sunny suggests the answer.
- **Content:** (internally `content`) the content of the answer. This appears in the description of the embed.
- **URL:** (internally `url`) this is an optional field that describes a remote resource relating to the answer. This can be a web article, a channel or message link, or anything that can be accessed by URL. It is included as a hyperlink on the embed title and as a button below the embed.
- **Query Reference:** (internally `query`) the query reference is a list of possible user queries that should trigger the answer. Sunny uses these to match a user's question.
- **Confidence:** (internally `minimumConfidence`) The minimum confidence level that Sunny must have to present this answer.

## List Answers

You can list answers with this command:

```
/index answer list <index>
```

This will show you all answers and their data.

## Add an Answer

You can add answers with this command:

```
/index answer add <index> <title> <content> [<url>] [<query>] [<minimum_confidence>]
```

`title` must not match another title in the same index. If not specified, `minimum_confidence` will default to 0.5 (50%).

## Remove an Answer

You can remove answers with this command:

```
/index answer remove <index> <title>
```

## Get Info About an Answer

You can get info about an answer with this command:

```
/index answer info <index> <title>
```

This will show you all the data of the answer.

## Edit an Answer

You can edit any field of an answer with this command:

```
/index answer edit <index> <title> [<new_title>] [<new_content>] [<new_url>] [<new_query>] [<new_minimum_confidence>]
```

`new_title` must not match another title in the same index.

## Add a Query to an Answer

You can add a query to an answer with this command:

```
/index answer add_query <index> <title> <query>
```

You can add multiple queries by separating them with semicolons.