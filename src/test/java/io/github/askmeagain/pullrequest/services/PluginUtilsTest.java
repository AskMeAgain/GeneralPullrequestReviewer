package io.github.askmeagain.pullrequest.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PluginUtilsTest {

  @Test
  void testEncodePath() {
    //Arrange --------------------------------------------------------------------------------
    var input = PluginUtils.encodePath("/abc def/test/file-abc.com");
    //Act ------------------------------------------------------------------------------------
    //Assert ---------------------------------------------------------------------------------
    Assertions.assertEquals(input, "%2Fabc%20def%2Ftest%2Ffile%2Dabc%2Ecom");
  }
}