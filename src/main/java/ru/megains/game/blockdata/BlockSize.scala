package ru.megains.game.blockdata

import ru.megains.game.physics.BlockAxisAlignedBB


sealed abstract class BlockSize(val value: Float) {

}

object BlockSize {

    case object Zero extends BlockSize(0)

    case object OneSixteenth extends BlockSize(1.0f / 16)

    case object TwoSixteenth extends BlockSize(2.0f / 16)

    case object ThreeSixteenth extends BlockSize(3.0f / 16)

    case object FourSixteenth extends BlockSize(4.0f / 16)

    case object FiveSixteenth extends BlockSize(5.0f / 16)

    case object SixSixteenth extends BlockSize(6.0f / 16)

    case object SevenSixteenth extends BlockSize(7.0f / 16)

    case object EightSixteenth extends BlockSize(8.0f / 16)

    case object NineSixteenth extends BlockSize(9.0f / 16)

    case object TenSixteenth extends BlockSize(10.0f / 16)

    case object ElevenSixteenth extends BlockSize(11.0f / 16)

    case object TwelveSixteenth extends BlockSize(12.0f / 16)

    case object ThirteenSixteenth extends BlockSize(13.0f / 16)

    case object FourteenSixteenth extends BlockSize(14.0f / 16)

    case object FifteenSixteenth extends BlockSize(15.0f / 16)

    case object One extends BlockSize(1.0f)

    val FULL_AABB = new BlockAxisAlignedBB(BlockSize.Zero, BlockSize.Zero, BlockSize.Zero, BlockSize.One, BlockSize.One, BlockSize.One)
    val NULL_AABB = new BlockAxisAlignedBB(BlockSize.Zero, BlockSize.Zero, BlockSize.Zero, BlockSize.Zero, BlockSize.Zero, BlockSize.Zero)

    def get(value: Float): BlockSize = {


        if (value < 0.5f / 16) {
            Zero
        } else if (value < 1.5f / 16) {
            OneSixteenth
        } else if (value < 2.5f / 16) {
            TwoSixteenth
        } else if (value < 3.5f / 16) {
            ThreeSixteenth
        } else if (value < 4.5f / 16) {
            FourSixteenth
        } else if (value < 5.5f / 16) {
            FiveSixteenth
        } else if (value < 6.5f / 16) {
            SixSixteenth
        } else if (value < 7.5f / 16) {
            SevenSixteenth
        } else if (value < 8.5f / 16) {
            EightSixteenth
        } else if (value < 9.5f / 16) {
            NineSixteenth
        } else if (value < 10.5f / 16) {
            TenSixteenth
        } else if (value < 11.5f / 16) {
            ElevenSixteenth
        } else if (value < 12.5f / 16) {
            TwelveSixteenth
        } else if (value < 13.5f / 16) {
            ThirteenSixteenth
        } else if (value < 14.5f / 16) {
            FourteenSixteenth
        } else if (value < 15.5f / 16) {
            FifteenSixteenth
        } else {
            One
        }
    }


}