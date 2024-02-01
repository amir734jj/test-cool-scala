class Main() extends IO() {
  var s1: Symbol = symbol("foo");
  var s2: Symbol = symbol("bar");
  var s3: Symbol = symbol("baz");

  var table: Hashtable = null;

  def setUp(): Unit = {
    table = new Hashtable()
  };

  def testEmpty(): Unit = {
    var enum: Enumeration = table.elements();

    // table size
    if (!(table.size() == 0)) out("[FAIL] empty table.size() != 0\n") else {};

    // elements
    if (is_null(enum)) {
      out("[FAIL] table.elements() == null\n")
    } else {
      if (enum.hasNext()) {
        out("[FAIL] empty table enumeration has elements\n")
      } else {}
    };

    var v1: Any = table.get(s1);
    var v2: Any = table.get(s2);
    var v3: Any = table.get(s3);

    // doesn't have items
    if (!is_null(v1)) out("[FAIL] empty table had value for 'foo'\n") else {};
    if (!is_null(v2)) out("[FAIL] empty table had value for 'bar'\n") else {};
    if (!is_null(v3)) out("[FAIL] empty table had value for 'baz'\n") else {};

    ()
  };

  def testSmall(): Unit = {
    var v1: Any = table.put(s1, "hi");
    var v2: Any = table.put(s2, true);
    var v3: Any = table.put(s3, 1234);

    // test size
    if (!(table.size() == 3)) out("[FAIL] table.size() != 3\n") else {};

    // can insert, won't return anything
    if (!is_null(v1)) out("[FAIL] empty table had replaced value for 'foo'\n") else {};
    if (!is_null(v2)) out("[FAIL] empty table had replaced value for 'bar'\n") else {};
    if (!is_null(v3)) out("[FAIL] empty table had replaced value for 'baz'\n") else {};

    // can retrieve put values
    if (!("hi" == table.get(s1))) out("[FAIL] table lost put value for key 'foo'\n") else {};
    if (!(true == table.get(s2))) out("[FAIL] table lost put value for key 'bar'\n") else {};
    if (!(1234 == table.get(s3))) out("[FAIL] table lost put value for key 'baz'\n") else {};

    var v4: Any = table.put(s1, "bye");
    var v5: Any = table.put(s2, false);
    var v6: Any = table.put(s3, 32145);

    // table size
    if (!(table.size() == 3)) out("[FAIL] table.size() != 3\n") else {};

    // update returns old value
    if (!("hi" == v4)) out("[FAIL] put doesn't return old value for key 'foo'\n") else {};
    if (!(true == v5)) out("[FAIL] put doesn't return old value for key 'bar'\n") else {};
    if (!(1234 == v6)) out("[FAIL] put doesn't return old value for key 'baz'\n") else {};

    // get returns updated value
    if (!("bye" == table.get(s1))) out("[FAIL] table lost updated value for key 'foo'\n") else {};
    if (!(false == table.get(s2))) out("[FAIL] table lost updated value for key 'bar'\n") else {};
    if (!(32145 == table.get(s3))) out("[FAIL] table lost updated value for key 'baz'\n") else {};

    var enum: Enumeration = table.elements();

    // elements
    if (is_null(enum)) {
      out("[FAIL] table.elements() == null\n")
    } else {
      var elements: ArrayAny = capture(enum, 3);

      contains(elements, s1, "bye");
      contains(elements, s2, false);
      contains(elements, s3, 32145)
    };

    ()
  };

  def testLarge(): Unit = {
    var i: Int = 0;
    while (i < 30) {
      table.put(symbol(i.toString()), i);
      i = i + 1
    };

    i = 0;
    while (i < 30) {
      var old: Any = table.put(symbol(i.toString()), i * 2);
      if (!(i == old)) out("[FAIL] put doesn't return old value for key ").out_any(i).out(".\n") else {};
      i = i + 1
    };

    // table size
    if (!(table.size() == 30)) out("[FAIL] large table.size() != 30\n") else {};

    var enum: Enumeration = table.elements();

    // elements
    if (is_null(enum)) {
      out("[FAIL] table.elements() == null\n")
    } else {
      var elements: ArrayAny = capture(enum, 30);
      ()
    };

    ()
  };

  def capture(enum: Enumeration, expected: Int): ArrayAny = {
    var i: Int = 0;
    var elements: ArrayAny = new ArrayAny(expected);

    while (enum.hasNext()) {
      if (i < expected) {
        elements.set(i, enum.next())
      } else {};

      i = i + 1
    };

    if (i < expected) {
      out("[FAIL] Enumeration returned too few elements.\n")
    } else {
      if (expected < i) {
        out("[FAIL] Enumeration returned too many elements.\n")
      } else {}
    };

    elements
  };

  def contains(elements: ArrayAny, key: Symbol, value: Any): Unit = {
    var i: Int = 0;
    var f: Boolean = false;
    while (i < elements.length()) {
      elements.get(i) match {
        case p: Pair => {
          if (p.first() == key) {
            f = true;
            if (!(p.second() == value)) {
              out("[FAIL] Key ").out_any(key).out(" has wrong value in enumeration.\n")
            } else {}
          } else {}
        }

        case null   => ()
        case x: Any => ()
      };

      i = i + 1
    };

    if (!f) out("[FAIL] Key ").out_any(key).out(" not present in table.\n") else {};
    ()
  };

  {
    out("[BEGIN TEST].\n");

    setUp(); testEmpty();
    setUp(); testSmall();
    setUp(); testLarge();

    out("[END TEST].\n")
  };
}