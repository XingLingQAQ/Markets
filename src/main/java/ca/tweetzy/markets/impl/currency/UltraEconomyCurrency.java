package ca.tweetzy.markets.impl.currency;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.markets.api.currency.IconableCurrency;
import ca.tweetzy.markets.settings.Settings;
import me.TechsCode.UltraEconomy.UltraEconomy;
import me.TechsCode.UltraEconomy.UltraEconomyAPI;
import me.TechsCode.UltraEconomy.objects.Account;
import me.TechsCode.UltraEconomy.objects.Currency;
import me.TechsCode.UltraEconomyAPI.base.item.XMaterial;
import org.bukkit.OfflinePlayer;

public final class UltraEconomyCurrency extends IconableCurrency {

	private final Currency currency;

	public UltraEconomyCurrency(String currencyName) {
		super("UltraEconomy", currencyName, "", CompMaterial.PAPER.parseItem());

		this.currency = UltraEconomy.getInstance().getCurrencies().name(currencyName).orElse(null);

		if (this.currency != null) {
			setDisplayName(this.currency.getName());

			if (Settings.CURRENCY_ICONS_OVERRIDE.getBoolean())
				setIcon(Settings.CURRENCY_ICONS.getItemStack());
			else
				setIcon(CompMaterial.EMERALD.parseItem()); // this.currency.getIcon().getAsItemStack().orElse(Settings.CURRENCY_ICONS.getItemStack())

			Currency vaultCurr = UltraEconomy.getInstance().getVaultCurrency().orElse(null);

			if (vaultCurr != null && vaultCurr.getKey().equalsIgnoreCase(this.currency.getKey()))
				setVault(true);
		}
	}

	@Override
	public boolean has(OfflinePlayer player, double amount) {
		final Account account = UltraEconomy.getInstance().getAccounts().uuid(player.getUniqueId()).orElse(null);
		if (account == null) return false;

		return account.getBalance(this.currency).getSum() >= amount;
	}

	@Override
	public boolean withdraw(OfflinePlayer player, double amount) {
		final Account account = UltraEconomy.getInstance().getAccounts().uuid(player.getUniqueId()).orElse(null);
		if (account == null) return false;

		account.removeBalance(this.currency, amount);
		return true;
	}

	@Override
	public boolean deposit(OfflinePlayer player, double amount) {
		final Account account = UltraEconomy.getInstance().getAccounts().uuid(player.getUniqueId()).orElse(null);
		if (account == null) return false;

		account.addBalance(this.currency, amount);
		return true;
	}
}

