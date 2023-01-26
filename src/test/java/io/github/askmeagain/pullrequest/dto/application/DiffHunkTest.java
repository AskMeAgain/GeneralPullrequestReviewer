package io.github.askmeagain.pullrequest.dto.application;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class DiffHunkTest extends TestCase {

  private final String input = "@@ -8,7 +8,7 @@ import org.springframework.web.bind.annotation.*;\n" +
      " @RestController\n" +
      " public class DemoApplication {\n" +
      " \n" +
      "-\t@GetMapping(\"/\")\n" +
      "+\t@GetMapping(\"/asdasdasdasd\")\n" +
      " \tString home() {\n" +
      " \t\treturn \"Spring is here!\";\n" +
      " \t}\n" +
      "@@ -16,4 +16,4 @@ public class DemoApplication {\n" +
      " \tpublic static void main(String[] args) {\n" +
      " \t\tSpringApplication.run(DemoApplication.class, args);\n" +
      " \t}\n" +
      "-}\n" +
      "\\ No newline at end of file\n" +
      "+}\n";

  @Test
  void test() {
    var hunk = new DiffHunk(input);

    Assert.assertEquals(false, hunk.isInHunk(3));
    Assert.assertEquals(false, hunk.isInHunk(8));
    Assert.assertEquals(false, hunk.isInHunk(9));
  }

}