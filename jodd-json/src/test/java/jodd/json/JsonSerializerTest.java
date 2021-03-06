// Copyright (c) 2003-2014, Jodd Team (jodd.org). All Rights Reserved.

package jodd.json;

import jodd.json.meta.JSON;
import jodd.json.meta.JsonAnnotationManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static jodd.util.ArraysUtil.bytes;
import static jodd.util.ArraysUtil.ints;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonSerializerTest {

	public static class Foo {

		protected String name;
		protected Long id;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public static class Bar {
		private Foo foo;
		private int number;

		public Foo getFoo() {
			return foo;
		}

		public void setFoo(Foo foo) {
			this.foo = foo;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}
	}

	public static class WhiteBar {
		@JSON
		private WhiteBar[] bars;
		private int sum;

		public WhiteBar[] getBars() {
			return bars;
		}

		public void setBars(WhiteBar[] bars) {
			this.bars = bars;
		}

		public int getSum() {
			return sum;
		}

		public void setSum(int sum) {
			this.sum = sum;
		}
	}

	public static class White {
		private int intensity;
		private Black black;

		public int getIntensity() {
			return intensity;
		}

		public void setIntensity(int intensity) {
			this.intensity = intensity;
		}

		public Black getBlack() {
			return black;
		}

		public void setBlack(Black black) {
			this.black = black;
		}
	}

	public static class Black {
		private int darkness;
		private White white;

		public int getDarkness() {
			return darkness;
		}

		public void setDarkness(int darkness) {
			this.darkness = darkness;
		}

		public White getWhite() {
			return white;
		}

		public void setWhite(White white) {
			this.white = white;
		}
	}

	// ---------------------------------------------------------------- tests

	@Test
	public void testSimpleMap() {
		Map map = new LinkedHashMap();

		map.put("one", "uno");
		map.put("two", "duo");

		JsonSerializer jsonSerializer = new JsonSerializer();
		String json = jsonSerializer.serialize(map);

		assertEquals("{\"one\":\"uno\",\"two\":\"duo\"}", json);

		map = new LinkedHashMap();
		map.put("one", Long.valueOf(173));
		map.put("two", Double.valueOf(7.89));
		map.put("three", Boolean.TRUE);
		map.put("four", null);
		map.put("five", "new\nline");

		jsonSerializer = new JsonSerializer();
		json = jsonSerializer.serialize(map);

		assertEquals("{\"one\":173,\"two\":7.89,\"three\":true,\"four\":null,\"five\":\"new\\nline\"}", json);
	}

	public static class InBean {
		HashMap<String, Object> params = new HashMap<String, Object>();
		ArrayList<String> names = new ArrayList<String>();

		public HashMap<String, Object> getParams() {
			return params;
		}

		public void setParams(HashMap<String, Object> params) {
			this.params = params;
		}

		public ArrayList<String> getNames() {
			return names;
		}

		public void setNames(ArrayList<String> names) {
			this.names = names;
		}
	}

	@Test
	public void testInMapVsInBeanbsInList() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("myid", Integer.valueOf(4343));
		ArrayList<String> names = new ArrayList<String>();
		names.add("veqna");

		// in map
		LinkedHashMap<String, Object> rootMap = new LinkedHashMap<String, Object>();
		rootMap.put("params", params);
		rootMap.put("names", names);

		JsonSerializer jsonSerializer = new JsonSerializer();
		String json = jsonSerializer.serialize(rootMap);

		Assert.assertEquals("{\"params\":{\"myid\":4343}}", json);

		// in bean
		InBean inBean = new InBean();
		inBean.setParams(params);
		inBean.setNames(names);

		jsonSerializer = new JsonSerializer();
		json = jsonSerializer.serialize(inBean);

		Assert.assertEquals("{}", json);

		// in list
		ArrayList list = new ArrayList();
		list.add(params);
		list.add(names);

		jsonSerializer = new JsonSerializer();
		json = jsonSerializer.serialize(inBean);

		Assert.assertEquals("{}", json);
	}

	@Test
	public void testSimpleObjects() {
		Foo foo = new Foo();
		foo.setName("jodd");
		foo.setId(Long.valueOf(976));

		Bar bar = new Bar();
		bar.setFoo(foo);
		bar.setNumber(575);

		JsonSerializer jsonSerializer = new JsonSerializer();
		String json = jsonSerializer.serialize(bar);

		assertEquals("{\"foo\":{\"id\":976,\"name\":\"jodd\"},\"number\":575}", json);
	}

	@Test
	public void testSimpleList() {
		List list = new LinkedList();

		list.add("one");
		list.add(new Bar());
		list.add(Double.valueOf(31E302));

		JsonSerializer jsonSerializer = new JsonSerializer();
		String json = jsonSerializer.serialize(list);

		assertEquals("[\"one\",{\"foo\":null,\"number\":0},3.1E303]", json);
	}

	@Test
	public void testSimpleArray() {
		int[] numbers = ints(1, 2, 3, 4, 5);

		JsonSerializer jsonSerializer = new JsonSerializer();
		String json = jsonSerializer.serialize(numbers);

		assertEquals("[1,2,3,4,5]", json);


		byte[] numbers2 = bytes((byte)1, (byte)2, (byte)3, (byte)4, (byte)5);

		json = jsonSerializer.serialize(numbers2);

		assertEquals("[1,2,3,4,5]", json);


		int[][] matrix = new int[][] {
				ints(1,2,3),
				ints(7,8,9)
		};

		json = jsonSerializer.serialize(matrix);

		assertEquals("[[1,2,3],[7,8,9]]", json);
	}

	@Test
	public void testEscapeChars() {
		String json = "\"1\\\" 2\\\\ 3\\/ 4\\b 5\\f 6\\n 7\\r 8\\t\"";

		String str = new JsonParser().parse(json);

		assertEquals("1\" 2\\ 3/ 4\b 5\f 6\n 7\r 8\t", str);

		String jsonStr = new JsonSerializer().serialize(str);

		assertEquals(json, jsonStr);
	}

	@Test
	public void testStrings() {
		String text = "Hello";

		String json = new JsonSerializer().serialize(new StringBuilder(text));
		assertEquals("\"Hello\"", json);

		json = new JsonSerializer().serialize(new StringBuffer(text));
		assertEquals("\"Hello\"", json);
	}

	@Test
	public void testChar() {
		Character character = Character.valueOf('J');

		String json = new JsonSerializer().serialize(character);

		assertEquals("\"J\"", json);
	}

	@Test
	public void testClass() {
		String json = new JsonSerializer().serialize(JoddJson.class);

		assertEquals("\"" + JoddJson.class.getName() + "\"", json);
	}

	@JSON(strict = false)
	public static class Cook {
		// no annotation
		private String aaa = "AAA";
		private String bbb = "BBB";
		private String ccc = "CCC";

		public String getAaa() {
			return aaa;
		}

		public void setAaa(String aaa) {
			this.aaa = aaa;
		}

		@JSON(include = false)
		public String getBbb() {
			return bbb;
		}

		public void setBbb(String bbb) {
			this.bbb = bbb;
		}

		@JSON(include = true)
		public String getCcc() {
			return ccc;
		}

		public void setCcc(String ccc) {
			this.ccc = ccc;
		}
	}

	@JSON(strict = true)
	public static class MasterCook extends Cook {
	}

	@Test
	public void testStrictMode() {
		Cook cook = new Cook();
		JsonAnnotationManager jam = JoddJson.annotationManager;

		JsonAnnotationManager.TypeData typeData = jam.lookupTypeData(Cook.class);

		assertEquals(1, typeData.rules.totalIncludeRules());
		assertEquals(1, typeData.rules.totalExcludeRules());

		assertEquals("ccc", typeData.rules.getRule(0));
		assertEquals("bbb", typeData.rules.getRule(1));

		JsonSerializer jsonSerializer = new JsonSerializer();

		String json = jsonSerializer.serialize(cook);

		assertTrue(json.contains("\"aaa\""));
		assertFalse(json.contains("\"bbb\""));
		assertTrue(json.contains("\"ccc\""));

		// now, strict = true, serialize only annotated properties!

		MasterCook masterCook = new MasterCook();

		typeData = jam.lookupTypeData(MasterCook.class);

		assertEquals(1, typeData.rules.totalIncludeRules());
		assertEquals(1, typeData.rules.totalExcludeRules());

		assertEquals("ccc", typeData.rules.getRule(0));
		assertEquals("bbb", typeData.rules.getRule(1));

		json = jsonSerializer.serialize(masterCook);

		assertFalse(json.contains("\"aaa\""));
		assertFalse(json.contains("\"bbb\""));
		assertTrue(json.contains("\"ccc\""));
	}

	@Test
	public void testCuriousModeOfSerialization() {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(Integer.valueOf(8));
		numbers.add(Integer.valueOf(4));
		numbers.add(Integer.valueOf(2));
		map.put("array", numbers);
		map.put("value", "BIG");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		map.put("list", list);

		Map<String, Object> val = new HashMap<String, Object>();
		val.put("name", "Root");
		val.put("value", "Hack");
		list.add(val);

		val = new HashMap<String, Object>();
		val.put("name", "John");
		val.put("value", "Protected");
		list.add(val);

		// serialize

		JsonSerializer jsonSerializer = new JsonSerializer();

		jsonSerializer.exclude("list");		// not applied
		jsonSerializer.include("array");
//		jsonSerializer.include("list");		// not needed, will be included by next two
		jsonSerializer.include("list.name");
		jsonSerializer.include("list.value");

		String str = jsonSerializer.serialize(map);

		Map<String, Object> result = new JsonParser().parse(str);

		assertEquals(map, result);
	}

	@Test
	public void testCircularDependenciesBean() {
		White white = new White();
		white.setIntensity(20);

		Black black = new Black();
		black.setDarkness(80);

		black.setWhite(white);

		white.setBlack(black);

		String json = new JsonSerializer().serialize(white);

		White whiteNew = new JsonParser().parse(json, White.class);

		assertEquals(white.getIntensity(), whiteNew.getIntensity());
		assertEquals(white.getBlack().getDarkness(), whiteNew.getBlack().getDarkness());
		assertNull(whiteNew.getBlack().getWhite());
	}

	@Test
	public void testCircularDependenciesMap() {
		Map<String, Object> white = new HashMap<String, Object>();
		white.put("intensity", Integer.valueOf(20));

		Map<String, Object> black = new HashMap<String, Object>();
		black.put("darkness", Integer.valueOf(80));

		black.put("white", white);
		white.put("black", black);

		String json = new JsonSerializer().serialize(white);

		Map<String, Object> whiteNew = new JsonParser().parse(json);

		assertEquals(white.get("intensity"), whiteNew.get("intensity"));
		assertEquals(
				((Map<String, Object>)(white.get("black"))).get("darkness"),
				((Map<String, Object>)(whiteNew.get("black"))).get("darkness"));
		assertNull(((Map<String, Object>) (whiteNew.get("black"))).get("black"));
		assertFalse(((Map<String, Object>) (whiteNew.get("black"))).containsKey("white"));
	}

	@Test
	public void testCircularDependenciesArray() {
		WhiteBar[] whiteBars = new WhiteBar[1];

		WhiteBar white = new WhiteBar();
		white.setSum(1);
		white.setBars(whiteBars);

		whiteBars[0] = white;

		String json = new JsonSerializer().serialize(whiteBars);

		assertEquals("[{\"sum\":1}]", json);
	}


}