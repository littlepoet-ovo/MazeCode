package com.sdxf.game;

public interface GameRunningData {
    public void Winning(int money);
    //游戏胜利执行改代码，参数为奖励给玩家的金币个数
    public boolean useMoney(int need);
    //途径收费站，传入参数"need"即通过该收费站需要花费的金币数，返回一个布尔值 true代表可以通过 false表示金币不足无法通过
    public void failed(boolean isReserve);
    //游戏失败执行该方法，传入一个参数，如果保留玩家原先的金币个数则提供true，如果需要从玩家数据库中扣除金币则提供false

}
