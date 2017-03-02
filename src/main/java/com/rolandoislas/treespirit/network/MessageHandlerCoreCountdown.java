package com.rolandoislas.treespirit.network;

import com.rolandoislas.treespirit.gui.renderer.CoreCountdownRenderer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Rolando on 3/1/2017.
 */
public class MessageHandlerCoreCountdown implements IMessageHandler<MessageCoreCountdown, IMessage> {
	@Override
	public IMessage onMessage(MessageCoreCountdown message, MessageContext ctx) {
		if (message.isCountingDown)
			CoreCountdownRenderer.startCountdown(message.deathTime, message.time);
		else
			CoreCountdownRenderer.stopCountdown();
		return null;
	}
}
