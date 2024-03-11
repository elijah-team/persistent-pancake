package tripleo.eljiah_pancake_durable.comp.i;

import tripleo.elijah_durable_pancake.comp.internal.LCM;

public record LCM_HandleEvent(LCM_CompilerAccess compilation,
                              LCM lcm, // TODO 11/24 this leaks. maybe should ignore
                              Object obj,
                              LCM_Event event) {
}
