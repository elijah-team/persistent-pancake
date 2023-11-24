package tripleo.elijah.comp.i;

import tripleo.elijah.comp.internal.LCM;

public record LCM_HandleEvent(LCM_CompilerAccess compilation,
                              LCM lcm, // TODO 11/24 this leaks. maybe should ignore
                              Object obj,
                              LCM_Event event) {
}
