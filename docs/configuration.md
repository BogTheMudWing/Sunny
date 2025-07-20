# Configuration

All configuration options can be edited through the `/configure` command. By default, only members with the Administrator permission can use this command.

## `no_correction_phrases`

Sunny applies spelling correction to queries to better match with answers. When Sunny responds with an answer, there is a footnote showing how the query was corrected. If Sunny is incorrectly changing a word or phrase, you can add it to the `no_correction_phrases` list to prevent it from being changed.

To list all phrases:

```
/configuration no_correction_phrases list
```

To add a phrase:

```
/configuration no_correction_phrases add <phrase>
```

To remove a phrase:

```
/configuration no_correction_phrases remove <phrase>
```

## `ignored_roles`

By default, Sunny responds to all user queries. You may want Sunny to ignore staff and support users. To do this, you can tell Sunny to ignore certain roles.

To list all ignored roles:

```
/configuration ignored_roles list
```

To add a role:

```
/configuration ignored_roles add <role>
```

To remove a role:

```
/configuration ignored_roles remove <role>
```

## Erase

To erase all data, including configuration and indexes, you can use the command:

```
/configure erase
```

Confirm by typing "ERASE ALL DATA" into the modal that appears.

::: danger
Erasing data is permanent and cannot be undone.
:::

When you remove the bot from your server, Sunny will automatically delete it after three days of being away.