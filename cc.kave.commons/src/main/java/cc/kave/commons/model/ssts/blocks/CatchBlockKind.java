package cc.kave.commons.model.ssts.blocks;

public enum CatchBlockKind {
	Default, // E.g., try { ... } catch(Exception e) { ... }
	Unnamed, // E.g., try { ... } catch(Exception) { ... }
	General // E.g., try { ... } catch { ... }
}
