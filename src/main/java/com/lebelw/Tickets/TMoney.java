package com.lebelw.Tickets;

import com.iConomy.*;
import com.iConomy.system.Holdings;
public class TMoney {

	private final Tickets plugin;
	public TMoney(Tickets instance) {
        plugin = instance;
    }
	public boolean checkIfEconomyPlugin() {
		if (plugin.iConomy != null){
			return true;
		}
		if (plugin.BOSEconomy != null){
			return true;
		}
		return false;
	}
	public boolean checkIfAccountExists(String name) {
		if (plugin.iConomy != null){
			return iConomy.hasAccount(name);
		}
		if (plugin.BOSEconomy != null){
			return plugin.BOSEconomy.playerRegistered(name,false);
		}
		return false;
		
	}
	public boolean removeMoney(String name, double amount){
		
		if (plugin.iConomy != null){
			Holdings balance = iConomy.getAccount(name).getHoldings();
			balance.subtract(amount);
		}
		if (plugin.BOSEconomy != null){
			
			int amountbose = (int) amount;
			return plugin.BOSEconomy.addPlayerMoney(name,amountbose,false);
		}
		return false;
	}
	public boolean checkIfEnough(String name, double amount){
		if (plugin.iConomy != null){
			Holdings balance = iConomy.getAccount(name).getHoldings();
			balance.hasEnough(amount);
		}
		if (plugin.BOSEconomy != null){
			int amountbose = (int) amount;
			int amountaccount = plugin.BOSEconomy.getPlayerMoney(name);
			if (amountaccount - amountbose >= 0)
				return true;
			else
				return false;
		}
		return false;
	}
	public String formatText(double amount){
		if (plugin.iConomy != null){
			return iConomy.format(amount);
		}
		if (plugin.BOSEconomy != null){
			int amountbose = (int) amount;
			if (amountbose > 1)
				return amountbose + plugin.BOSEconomy.getMoneyNamePluralCaps();
			else
				return amountbose + plugin.BOSEconomy.getMoneyNameCaps();
		}
		return "Undefined";
		
		
	}
}
