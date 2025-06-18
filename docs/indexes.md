---

outline: deep

---


# Managing Indexes

## Creating an Index

You can create an index with a command.

```
/index create <name> [<channel>]
```

::: info
Arguments like `<this>` are required. Arguments like `[<that>]` are optional.
:::

By default, this is limited to users with the **Manage Channels** permission.

- `name` must be unique.

## Deleting an Index

You can delete an index with a command.

```
/index delete <name>
```

By default, this is limited to users with the **Manage Channels** permission.

::: warning
Deleting an index is permanent and cannot be undone.
:::

## Channel Links

An index can be linked to any number of channels. When a user asks a question in a channel, Sunny will add the answers of any index with that channel linked.

This allows you, for example, to handled a situation like this:

- **#questions** for general questions.
- **#api-support** for API questions.
- **#usage-help** for end user questions.

You can create a **Server** index for server answers, an **API** index for API answers, and a **Usage** index for usage answers. Then, you can link the channels like so:

- **Server**
    - **#questions**
- **API**
    - **#questions**
    - **#api-support**
- **Usage**
    - **#questions**
    - **#usage-help**

Sunny will have answers for API questions in **#api-support**, usage questions in **#usage-help**, and all questions in **#questions**.

| Channel          | Server | API | Usage |
| ---------------- | :----: | :-: | :---: |
| **#questions**   | ✔      | ✔   | ✔     |
| **#api-support** | -      | ✔   | -     |
| **#usage-help**  | -      | -   | ✔     |

### Linking

You can link a channel to an index with a command.

```
/index channel link <channel> <index>
```

By default, this is limited to users with the **Manage Channels** permission.

### Unlinking

You can unlink a channel from an index with a command.

```
/index channel unlink <channel> <index>
```

By default, this is limited to users with the **Manage Channels** permission.

### Listing

You can list the channels that an index is linked to with a command.

```
/index channel list_channels <index>
```

You can also list all the indexes that a channel is linked to with a command.

```
/index channel list_indexes <channel>
```

By default, these commands are limited to users with the **Manage Channels** permission.