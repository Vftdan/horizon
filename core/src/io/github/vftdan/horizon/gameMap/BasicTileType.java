package io.github.vftdan.horizon.gameMap;


public enum BasicTileType {
		OPAQUE {
			public boolean isOpaque() {
				return true;
			}

			public boolean isSemi() {
				return false;
			}

			public BasicTileType toSemi() {
				return SOME_SEMI;
			}
		}, PERMEABLE {
			public boolean isOpaque() {
				return false;
			}

			public boolean isSemi() {
				return false;
			}

			public BasicTileType toSemi() {
				return ALL_SEMI;
			}
		}, ALL_SEMI {
			public boolean isOpaque() {
				return false;
			}

			public boolean isSemi() {
				return true;
			}

			public BasicTileType toSemi() {
				return this;
			}
		}, SOME_SEMI {
			public boolean isOpaque() {
				return true;
			}

			public boolean isSemi() {
				return true;
			}

			public BasicTileType toSemi() {
				return this;
			}
		};
			public static BasicTileType create(boolean opaque, boolean semi) {
				if(opaque) {
					if(semi) return SOME_SEMI;
					return OPAQUE;
				}
				if(semi) return ALL_SEMI;
				return PERMEABLE;
			}
			public abstract boolean isOpaque();
			public abstract boolean isSemi();
			public abstract BasicTileType toSemi();
		}