package barracuda;

import com.gikk.twirk.events.TwirkListener;
import com.gikk.twirk.events.TwirkListenerBaseImpl;
import com.gikk.twirk.types.twitchMessage.TwitchMessage;
import com.gikk.twirk.types.users.TwitchUser;

import java.io.IOException;
import java.util.Scanner;

import com.gikk.twirk.*;


public class Main {
	
	public final String MY_PASS = "oauth:stsqsg4w2pa7e7lgvg38f21bg0ytbg";
	public final String MY_ID = "fluxxsc";
	
	public Main() {
		try {
			setup();
		} catch (IOException e) {
			System.out.println(e);
			return;
		} catch (InterruptedException e) {
			System.out.println(e);
			return;
		}
		
	}
	
	public void setup() throws IOException, InterruptedException{
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Streamer name: ");
		String channel = "#" + scanner.nextLine();
		
		System.out.println("Connecting to " + channel);
		
		Twirk twirk = new TwirkBuilder(channel, MY_ID, MY_PASS)
				.setVerboseMode(false)
				.build();
		
		twirk.addIrcListener( onDisconnectListener(twirk));
		twirk.addIrcListener( onPrivMsgListener(twirk));
		
		twirk.connect();
		
		String line;
		while ( !(line = scanner.nextLine()).matches(".quit") ) {
			twirk.channelMessage(line);
		}
		
		scanner.close();
		twirk.close();
	}
	
	private TwirkListener onDisconnectListener(final Twirk twirk) {
		return new TwirkListenerBaseImpl() {
			@Override
			public void onDisconnect() {
				try {
					if (!twirk.connect()) {
						twirk.close();
					}
				} catch (IOException e) {
					System.out.println(e);
					twirk.close();
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
		};
	}
	
	private TwirkListener onPrivMsgListener(final Twirk twirk) {
		return new TwirkListenerBaseImpl() {
			@Override
			public void onPrivMsg(TwitchUser sender, TwitchMessage message) {
				System.out.println(sender.getDisplayName() + ": " + message.getContent());
			}
		};
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
