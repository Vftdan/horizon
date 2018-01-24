package io.github.vftdan.horizon.scripting;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

public class FilteredContextFactory extends ContextFactory {
	@Override
	protected Context makeContext() {
		Context cx = super.makeContext();
		cx.setWrapFactory(new FilteredWrapFactory());
		return cx;
	}
}
