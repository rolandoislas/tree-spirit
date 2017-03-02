package com.rolandoislas.treespirit.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by Rolando on 3/1/2017.
 */
public class MessageCoreCountdown implements IMessage {
	long time;
	boolean isCountingDown;
	int deathTime;

	/**
	 * Death countdown message
	 * @param deathTime time in seconds before death
	 * @param time ticks that have elapsed
	 */
	public MessageCoreCountdown(int deathTime, long time) {
		this.isCountingDown = true;
		this.deathTime = deathTime;
		this.time = time;
	}

	public MessageCoreCountdown() {
		this.isCountingDown = false;
		this.deathTime = 0;
		this.time = 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		isCountingDown = buf.getBoolean(0);
		deathTime = buf.getInt(1);
		time = buf.getLong(5);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(isCountingDown);
		buf.writeInt(deathTime);
		buf.writeLong(time);
	}
}
