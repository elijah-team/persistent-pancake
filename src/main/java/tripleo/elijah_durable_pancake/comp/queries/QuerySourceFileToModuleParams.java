package tripleo.elijah_durable_pancake.comp.queries;

import java.io.InputStream;

public record QuerySourceFileToModuleParams(
  InputStream inputStream,

  /**
   * @param aSourceFilename pass this to elijjah parser and lexer
   */
  String sourceFilename
) { }
