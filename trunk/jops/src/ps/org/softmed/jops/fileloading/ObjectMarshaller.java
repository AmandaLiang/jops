package org.softmed.jops.fileloading;

import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Vector3f;


public class ObjectMarshaller implements IMarshaller, IUnmarshaller, IAliasable {
	private String m_uri;

	private int m_index;

	private String m_name;

	String TYPE = "type";

	String FLOAT = "float";

	String COLOR = "color";

	String VECTOR = "vector";
	
	String POINT = "point";

	SimpleColorConverter colorConverter = new SimpleColorConverter();

	SimpleVectorConverter vectorConverter = new SimpleVectorConverter();
	
	SimplePointConverter pointConverter = new SimplePointConverter();

	public ObjectMarshaller() {
		m_uri = null;
		m_index = 0;
		m_name = "hashmap";
	}

	public ObjectMarshaller(String uri, int index, String name) {
		m_uri = uri;
		m_index = index;
		m_name = name;
	}

	public boolean isExtension(int arg0) {
		return false;
	}

	public boolean isPresent(IUnmarshallingContext arg0) throws JiBXException {
		return arg0.isAt(m_uri, m_name);
	}

	public void marshal(Object arg0, IMarshallingContext arg1)
			throws JiBXException {

		if (arg0 instanceof Float) {

			MarshallingContext ctx = (MarshallingContext) arg1;

			ctx.startTagAttributes(m_index, m_name).attribute(m_index, TYPE,
					FLOAT).closeStartContent();
			ctx.content(((Float) arg0).toString());

			/*
			 * ctx.startTagAttributes(m_index, "FLOAT"); ctx.attribute(m_index,
			 * "CHAVE", ((Float)arg0).toString()); ctx.closeStartContent(); //
			 */
			ctx.endTag(m_index, m_name);
		} else if (arg0 instanceof Colorf) {
			MarshallingContext ctx = (MarshallingContext) arg1;

			ctx.startTagAttributes(m_index, m_name).attribute(m_index, TYPE,
					COLOR).closeStartContent();
			ctx.content(colorConverter.toString(arg0));

			ctx.endTag(m_index, m_name);
		} else if (arg0 instanceof Vector3f) {
			MarshallingContext ctx = (MarshallingContext) arg1;

			ctx.startTagAttributes(m_index, m_name).attribute(m_index, TYPE,
					VECTOR).closeStartContent();
			ctx.content(vectorConverter.toString(arg0));

			ctx.endTag(m_index, m_name);
		}else if (arg0 instanceof Point3f) {
			MarshallingContext ctx = (MarshallingContext) arg1;

			ctx.startTagAttributes(m_index, m_name).attribute(m_index, TYPE,
					POINT).closeStartContent();
			ctx.content(pointConverter.toString(arg0));

			ctx.endTag(m_index, m_name);
		}
	}

	public Object unmarshal(Object arg0, IUnmarshallingContext arg1)
			throws JiBXException {

		// make sure we're at the appropriate start tag
		UnmarshallingContext ctx = (UnmarshallingContext) arg1;
		if (!ctx.isAt(m_uri, m_name)) {
			ctx.throwStartTagNameError(m_uri, m_name);
		}

		ctx.parseToStartTag(m_uri, m_name);

		String type = ctx.attributeText(m_uri, TYPE);
		ctx.parsePastStartTag(m_uri, m_name);

		String text = ctx.parseContentText();

		ctx.parsePastEndTag(m_uri, m_name);

		Object obj = null;
		if (type.equals(FLOAT))
			obj = new Float((String) text);
		else if (type.equals(COLOR))
			obj = colorConverter.fromString(text);
		else
		if (type.equals(VECTOR))
			obj = vectorConverter.fromString(text);
		else
			if (type.equals(POINT))
				obj = pointConverter.fromString(text);

		return obj;
	}

}
