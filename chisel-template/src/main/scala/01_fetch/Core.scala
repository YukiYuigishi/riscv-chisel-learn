package fetch

import chisel3._
import chisel3.util._
import common.Consts._

class Core extends Module{
    val io = IO(new Bundle{
        /* ImemPortIoをインスタンス化したものを反転
            出力ポートaddr, 入力ポートinstを生成 */
        val imem = Flipped(new ImemPortIo())
        //出力ポートaddr, および入力ポートinstを作成
        val exit = Output(Bool())
    })

    /* 32bit幅 x32本のレジスタを生成 
        WORD_LEN = 32 */
    val regfile = Mem(32, UInt(WORD_LEN.W))

    // **************
    // Instruction Fetch (IF) Stage

    /*  初期値を０とするPCレジスタを生成
        サイクルごとに4ずつカウントアップ
        START_ADDR = 0 
     */
    val pc_reg = RegInit(START_ADDR)
    pc_reg := pc_reg + 4.U(WORD_LEN.W)

    //出力ポートaddr に pc_regを接続し、入力ポートinstをinstで受ける
    io.imem.addr := pc_reg
    val inst = io.imem.inst

    // inst が 34333231 の場合にtrue.B
    io.exit := (inst === 0x34333231.U(WORD_LEN.W))
    
    printf(p"addr : 0x${Hexadecimal(io.imem.addr)}\n")
    printf(p"pc_reg : 0x${Hexadecimal(pc_reg)}\n")
    printf(p"inst : 0x${Hexadecimal(inst)}\n")
    printf("----------\n")


}