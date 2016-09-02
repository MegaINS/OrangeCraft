package ru.megains.engine.graph.text;


import org.joml.Vector3f;
import ru.megains.engine.graph.renderer.mesh.Mesh;
import ru.megains.engine.graph.renderer.mesh.MeshMaker$;

public class Text {


    private static final float ZPOS = 0.0f;
    private static final String ascii = "font/ascii";
    private static final int[] charWidth = new int[256];

    static {
        for (int i = 0; i < charWidth.length; i++) {
            switch (i) {
                case 32:
                    charWidth[i] = 5;
                    break;
                case 44:
                case 46:
                case 58:
                case 59:
                case 102:
                case 105:
                case 108:

                    charWidth[i] = 3;
                    break;
                case 116:
                    charWidth[i] = 5;
                    break;
                default:
                    charWidth[i] = 7;
            }
        }
    }

    private String text;
    private Mesh mesh;
    private Vector3f rotation;
    private Vector3f position;
    public float scale = 2;

    public Text(String text) {

        this.text = text;

        setMesh(buildMesh());
        rotation = new Vector3f(0, 0, 0);
    }

    private Mesh buildMesh() {

        char[] characters = text.toCharArray();

        MeshMaker$ mm = MeshMaker$.MODULE$;
        mm.startMakeTriangles();

        mm.setTexture(ascii);
        float startx = 0;
        float a = 1f / 128f;
        float b;
        float u;
        float v;
        for (char character : characters) {
            b = charWidth[(int) character];
            v = (int) character / 16;
            u = (int) character - 16 * v;


            mm.setCurrentIndex();

            mm.addVertexWithUV(startx, 8, ZPOS, u * 8 * a, v * 8 * a);
            mm.addVertexWithUV(startx, 0.0f, ZPOS, u * 8 * a, (v * 8 + 8) * a);
            mm.addVertexWithUV(startx + b, 0.0f, ZPOS, (u * 8 + b) * a, (v * 8 + 8) * a);
            mm.addVertexWithUV(startx + b, 8, ZPOS, (u * 8 + b) * a, v * 8 * a);
            mm.addIndex(0);
            mm.addIndex(1);
            mm.addIndex(2);
            mm.addIndex(3);
            mm.addIndex(0);
            mm.addIndex(2);
            startx += b;
        }

        return mm.makeMesh();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        mesh.cleanUp();
        mesh = null;
        mesh = buildMesh();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public void setPosition(float x, float y, int z) {
        position = new Vector3f(x, y, z);
    }


}