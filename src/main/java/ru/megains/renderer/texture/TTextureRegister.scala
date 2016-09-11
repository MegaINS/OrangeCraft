package ru.megains.renderer.texture


trait TTextureRegister {

    def registerTexture(textureName: String): TextureAtlas

}
