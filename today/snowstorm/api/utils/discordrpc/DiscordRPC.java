package today.snowstorm.api.utils.discordrpc;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRichPresence;
import store.snowstorm.UserAccount;
import today.snowstorm.client.Snowstorm;

public class DiscordRPC  extends Thread {

    // Discord RichPresence Thread.

    // DiscordID
    private final static String ID = "1059564833768493096";

    private static boolean hookedToDiscord = false;

    @Override
    public void run() {

        // In the event of shutdown, unhook from Discord
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Suspending Discord RichPresence Thread.");
            hookedToDiscord = false;
            net.arikia.dev.drpc.DiscordRPC.discordShutdown();
        }));

        DiscordEventHandlers handler = new DiscordEventHandlers.Builder().setReadyEventHandler((account) -> {
            System.out.println("Hooked into Discord!");
            hookedToDiscord = true;
        }).build();

        net.arikia.dev.drpc.DiscordRPC.discordInitialize(ID, handler, true);
        net.arikia.dev.drpc.DiscordRPC.discordRegister(ID, "");

        // Loop
        while(true) {
            net.arikia.dev.drpc.DiscordRPC.discordRunCallbacks();

            if(hookedToDiscord) {
                String textOne = "";
                String textTwo = "Awaiting Authentication...";

                if(UserAccount.account != null) {
                    textOne = "Username: " + UserAccount.account.getName();
                    textTwo = "UID: " + UserAccount.account.getUid();
                }

                DiscordRichPresence.Builder rpc = new DiscordRichPresence.Builder(textTwo);
                rpc.setDetails(textOne);

                rpc.setBigImage("snowstorm_today", "Snowstorm " + Snowstorm.INSTANCE.DISTRO + " " + Snowstorm.INSTANCE.build);

                //System.out.println("Updated DiscordRPC.");

                net.arikia.dev.drpc.DiscordRPC.discordUpdatePresence(rpc.build());

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    // Do nothing rn
                }
            } else {
                System.out.println("Error hooking to Discord for RichPresence, retrying.");
                hookedToDiscord = false;
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    // Do nothing rn
                }
            }
        }
    }

}