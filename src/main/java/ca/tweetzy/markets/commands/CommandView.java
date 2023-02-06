package ca.tweetzy.markets.commands;

import ca.tweetzy.flight.command.AllowedExecutor;
import ca.tweetzy.flight.command.Command;
import ca.tweetzy.flight.command.ReturnType;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.markets.Markets;
import ca.tweetzy.markets.api.market.Market;
import ca.tweetzy.markets.gui.shared.view.content.MarketViewGUI;
import ca.tweetzy.markets.settings.Translations;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class CommandView extends Command {

	public CommandView() {
		super(AllowedExecutor.PLAYER, "view");
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		if (args.length < 1) return ReturnType.INVALID_SYNTAX;

		if (sender instanceof final Player player) {
			final OfflinePlayer target = Bukkit.getPlayerExact(args[0]);
			Market market = null;

			if (target == null) {
				market = Markets.getMarketManager().getByOwnerName(args[0]);

				if (market == null) {
					Common.tell(player, TranslationManager.string(player, Translations.PLAYER_OFFLINE, "value", args[0]));
					return ReturnType.FAIL;
				}
			}

			if (market == null)
				market = Markets.getMarketManager().getByOwner(target.getUniqueId());

			if (market == null) {
				Common.tell(player, TranslationManager.string(player, Translations.NO_MARKET_FOUND, "player_name", args[0]));
				return ReturnType.FAIL;
			}

			Markets.getGuiManager().showGUI(player, new MarketViewGUI(player, market));
		}

		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "markets.command.view";
	}

	@Override
	public String getSyntax() {
		return "view <player>";
	}

	@Override
	public String getDescription() {
		return "Used to open another player's market";
	}
}
