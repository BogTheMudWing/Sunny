package org.macver.sunny;

public class Reporter {

    public void report(String string) {
        Sunny.jda.retrieveUserById(Sunny.config.sendReportsTo).queue(
                user -> user.openPrivateChannel().complete().sendMessage(string.substring(0,1999)).complete()
        );
    }

}
