# JSON Format

All Sunny data for your guild is stored in JSON format.

```json
{
    "indexes": [ // The list of indexes
        { // An index.
            "name": "Server", // The name of the index.
            "channels": [ // The channels linked to this index.
                1008590675106873484
            ],
            "answers": [ // The answers for this index.
                {
                    "title": "Joining the Staff Team", // Must be unique.
                    "content": "We aren't looking for new staff members right now. We open applications from time to time, so keep an eye on the announcements channel.",
                    "url": "https://docs.woftnw.org/books/admin-handbook/chapter/staff-positions", // Optional.
                    "query": [
                        "How do I become staff?",
                        "Can I be a mod?",
                        "When are helper applications?",
                        "Can I have admin?",
                        "I want to join the staff.",
                        "Are staff apps open?"
                    ],
                    "minimumConfidence": 0.5

                }
            ]
        }
    ],
    "noCorrectionPhrases": [ // Words and phrases to not autocorrect.
        "server ip"
    ]
}
```

The file represents a list of indexes. Each index contains a name, a list of linked channels, and a list of answers.

You can export your guild data as JSON.

```
/data export
```

Sunny will give you a JSON file containing all your guild data.

You can also import your own JSON file if you don't want to use the commands.

```
/data import file:import.json
```

The name of the file doesn't matter as long as it ends in `.json`. Sunny will check that your file is valid and confirm that you want to overwrite your existing data.

::: danger
Importing JSON data will completely erase and overwrite your existing settings.
:::