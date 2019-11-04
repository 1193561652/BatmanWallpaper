package org.obj2openjl.v3.model;


import java.util.List;

public class OpenGLModelData {
	
	private float[] vertices;
	private float[] normals;
	private float[] textureCoordinates;
	private short[] indices;
	private List<String> usemtl;

	public OpenGLModelData(
			float[] vertices,
			float[] normals,
			float[] textureCoordinates,
			short[] indices) {
		this.vertices = vertices;
		this.normals = normals;
		this.textureCoordinates = textureCoordinates;
		this.indices = indices;
	}

	public OpenGLModelData(
            float[] vertices,
            float[] normals,
            float[] textureCoordinates,
            short[] indices,
            List<String> usemtl) {
		this.vertices = vertices;
		this.normals = normals;
		this.textureCoordinates = textureCoordinates;
		this.indices = indices;
		this.usemtl = usemtl;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getNormals() {
		return normals;
	}

	public float[] getTextureCoordinates() {
		return textureCoordinates;
	}

	public short[] getIndices() {
		return indices;
	}

	public List<String> getUsemtl() {
	    return usemtl;
	}
}
