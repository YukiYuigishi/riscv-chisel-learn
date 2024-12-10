package decode

import chisel3._
import chisel3.util._
import common.Consts._

class Core extends Module {
  val io = IO(new Bundle {
    /* ImemPortIoをインスタンス化したものを反転
            出力ポートaddr, 入力ポートinstを生成 */
    val imem = Flipped(new ImemPortIo())
    // 出力ポートaddr, および入力ポートinstを作成
    val exit = Output(Bool())
  })

  /* 32bit幅 x32本のレジスタを生成
        WORD_LEN = 32 */
  val regfile = Mem(32, UInt(WORD_LEN.W))

  // *******************************
  // Instruction Fetch (IF) Stage

  /*  初期値を０とするPCレジスタを生成
        サイクルごとに4ずつカウントアップ
        START_ADDR = 0
   */
  val pc_reg = RegInit(START_ADDR)
  pc_reg := pc_reg + 4.U(WORD_LEN.W)

  // 出力ポートaddr に pc_regを接続し、入力ポートinstをinstで受ける
  io.imem.addr := pc_reg
  val inst = io.imem.inst

  // *******************************
  // Instruction Decode (ID) Stage
  // レジスタ番号の解読
  val rs1_addr = inst(19, 15)
  val rs2_addr = inst(24, 20)
  val wb_addr = inst(11, 7)
  // レジスタデータの読み出し
  val rs1_data = Mux((rs1_addr =/= 0.U(WORD_LEN.U)), regfile(rs1_addr), 0.U(WORD_LEN.W))
  val rs2_data = Mux((rs2_addr =/= 0.U(WORD_LEN.U)), regfile(rs2_addr), 0.U(WORD_LEN.W))

  // inst が 34333231 の場合にtrue.B
  io.exit := (inst === 0x34333231.U(WORD_LEN.W))

  printf(p"pc_reg : 0x${Hexadecimal(pc_reg)}\n")
  printf(p"inst : 0x${Hexadecimal(inst)}\n")

  // ID Debug
  printf(p"rs1_addr : $rs1_addr\n")
  printf(p"rs2_addr : $rs2_addr\n")
  printf(p"wb_addr :$wb_addr\n")
  printf(p"rs1_data : 0x${Hexadecimal(rs1_data)}\n")
  printf(p"rs2_data : 0x${Hexadecimal(rs2_data)}\n")
  printf("----------\n")

}
