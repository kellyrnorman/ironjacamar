/*
 * IronJacamar, a Java EE Connector Architecture implementation
 * Copyright 2015, Red Hat Inc, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the Eclipse Public License 1.0 as
 * published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse
 * Public License for more details.
 *
 * You should have received a copy of the Eclipse Public License 
 * along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ironjacamar.common.metadata.ds;

import org.ironjacamar.common.api.metadata.ds.DataSources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Expression tests
 * @author <a href="mailto:jesper.pedersen@ironjacamar.org">Jesper Pedersen</a>
 */
public class ExpressionTestCase
{
   /**
    * Write
    * @throws Exception In case of an error
    */
   @Test
   public void testWrite() throws Exception
   {
      DsParser parser = new DsParser();

      InputStream is = ExpressionTestCase.class.getClassLoader().
         getResourceAsStream("../../resources/test/ds/expression.xml");
      assertNotNull(is);

      StringBuilder sb = new StringBuilder();
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      String line = br.readLine();
      while (line != null)
      {
         String data = line.trim();
         sb.append(data);
         if (!data.equals("") && !data.endsWith(">"))
            sb.append(" ");
         line = br.readLine();
      }

      is.close();

      is = ExpressionTestCase.class.getClassLoader().
         getResourceAsStream("../../resources/test/ds/expression.xml");
      assertNotNull(is);
      
      XMLStreamReader xsr = XMLInputFactory.newInstance().createXMLStreamReader(is);

      DataSources ds = parser.parse(xsr);
      assertNotNull(ds);

      is.close();

      StringWriter sw = new StringWriter();
      XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(sw);
      xsw.setDefaultNamespace("");

      xsw.writeStartDocument("UTF-8", "1.0");
      parser.store(ds, xsw);
      xsw.writeEndDocument();

      xsw.flush();
      xsw.close();

      assertEquals(sb.toString(), sw.toString());
   }
}
