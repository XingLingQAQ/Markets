package ca.tweetzy.markets.listeners;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.markets.Markets;
import ca.tweetzy.markets.api.event.MarketTransactionEvent;
import ca.tweetzy.markets.api.market.Transaction;
import ca.tweetzy.markets.impl.MarketTransaction;
import ca.tweetzy.markets.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public final class MarketTransactionListener implements Listener {

	@EventHandler
	public void onTransactionEvent(final MarketTransactionEvent event) {
		final Transaction transaction = new MarketTransaction(
				UUID.randomUUID(),
				event.getBuyer().getUniqueId(),
				event.getBuyer().getName(),
				event.getSeller().getUniqueId(),
				event.getSeller().getUniqueId().equals(UUID.fromString(Settings.SERVER_MARKET_UUID.getString())) ? Settings.NAME.getString() : event.getSeller().getName(),
				event.getType(),
				event.getItem(),
				event.getCurrency(),
				event.getQuantity(),
				event.getPrice(),
				System.currentTimeMillis()
		);

		transaction.store(storeTransaction -> {
			if (storeTransaction == null)
				Common.log("&CSomething went wrong while trying to store transaction: &d" + transaction.getId().toString());
			else
				Markets.getTransactionManager().add(storeTransaction);
		});

		// create an offline notification for the player

	}
}
