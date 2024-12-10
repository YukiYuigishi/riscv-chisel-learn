package fetch


import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile
import common.Consts._

class ImemPortIo extends Bundle{
    val addr = Input(UInt(WORD_LEN.W))
    val inst = Output(UInt(WORD_LEN.W))
}

class Memory extends Module{
    val io = IO(new Bundle{
        val imem = new ImemPortIo()
    })
    /* メモリの実態として、8bit幅x16384本(16KB) のレジスタを生成
        8bit幅である理由はPCのカウントアップ幅を4にするため
        1アドレス8bit 4アドレスに32bit格納する
    */
    val mem = Mem(16384, UInt(8.W))

    // メモリデータをロード
    loadMemoryFromFile(mem, "src/hex/fetch.hex")

    // 各アドレスに格納された8bitデータを4つつなげて32bitデータに
    io.imem.inst := Cat(
        mem(io.imem.addr + 3.U(WORD_LEN.W)),
        mem(io.imem.addr + 2.U(WORD_LEN.W)),
        mem(io.imem.addr + 1.U(WORD_LEN.W)),
        mem(io.imem.addr)
    )
}
