package freesoccerhdx.survivalplus.npc;

public enum NPCSkins {

	JEFFERSON("Jefferson","ewogICJ0aW1lc3RhbXAiIDogMTU5MTIxMjcyODk5NCwKICAicHJvZmlsZUlkIiA6ICI2NTc3OGE5YWUzYTE0MTI5ODVlN2RjNTdhMzc3NTE1YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYXJ0b3BoIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U5NWQ1NTk0ZWRjY2U4YWZhZGU2YmEyNGVlNTBkNmM3NGU4M2I5NjAwNTE0NjBlZGEyNWZlZTQ3NzRkNmNmYzMiCiAgICB9CiAgfQp9",
			"x4txELQZit88KubBG6BYhvAud0ewfGfKC1rZe5V42jKcbMXEfCO2QDA35xEPQxUbTLppPM4oOX9PsGAr2kYl+ZZLyFKDx3ibCE0Y3Ft/NnRtOOi9b5INao+KSEPYBbTnyCvyG3eF2evFRYZQ1X4OXCm4rHF7OKnTHMjNsryOAhbXZFh1tQnMMZfzegCVlOPqroiHdD7tx60SVqOrCD2U1qm4ZFLX5V0FXjrkMqDPw6FvLf4cnJiYZ0M/qbyD0EFr4uf06bOavYoB64c3JmR6mdGUSQzpCezFE+l6Mm0JAtlflAjpU5iDxrFihMvaRkVEtrtVKDjye53I/HCwLx87vg1mTr73IFGfKeu5KnqBmZpV/m4+m1FCrqLPeCiTo95U+CGLfwLJY7OyrDNjRVRbuHvh23dYUIL9Osr3STdrbkJUnhp1DozFXkBKHWrT702oB+/wvopEdeSq/GvNXeFBnmML/K8E9coahFxWd/eTV0Sc+V/8igWVoFILb9yeafKoKBIV9aUgcOWO/ghS6fBBstrANUdY9R75ombhaKbr8kj01nuWDj20WhB1eZr6XlFfzOSeb7Op4QcetrBZdhy2KA5iIQT2mO+bx3k4v9pSNsorHmIozSGQTbEe4FOY64L9zBQlswRyvCHR06nLZSRXU+bMBkX6abhF9ezMkYABWm8="),
	BARKEEPER("Barkeeper","ewogICJ0aW1lc3RhbXAiIDogMTU5MTIxMjYxODIxNiwKICAicHJvZmlsZUlkIiA6ICJmMDhlOTcyM2E2OWE0Y2FkYjUwZDUzODIwYjUxMjIzNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCYXJrZWVwZXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE0NDBkMDc3ZWU4NTdhNGI4MGNiNTY3ZGRkNzRlNDI1YjVhYzNkODhjMjcwMjU2ZjEyYzk5YjMwM2Y2ZWY4YSIKICAgIH0KICB9Cn0=",
		    "jq4m6I+XdjZfc720vm53PgqgN+WLeykTdBUj4Nv3KfbtbnhdWBffTELvEgdsZuLK9PIEDPt99mw6lLePZTBr+e9/HqQqsSLxFxew658g4+g72BlBVdboUTwYOE7H9lKf6kklHmSQkBNXIlU1hcotP+7wuuZw+sgf12CaIG9wLJK5KNkXfCvjkg3A01qsWu4ZzcSYId0Olw6G+zgIjGrOczV3p8GMYxIqW3ortJ3RZ2JIb1LL8pUj1J280QwZaiE4Ifp2BkomSD2q8c5ChWyF/nNCJaQ6f46jL/22GdCrqoSv5bwl9vKoWBmCfGvS8p4+n60+afKOGD1Qrpc1TXIYtiWGYyaX5Zio/RYiE/6VZ18XestV/md6I1fc45XvP60oxLHgPZBsUGS9tPRt6brtgSlZBfJ7EDNGDj6W9xXfC+oqOTKYHtZT1ENzxZAU+Y4LTrdO2lrx/GaQ1X3RXdA87tHO5JeC2b28wtY8S7Lz2WBOSos2taPyt2YzWUN420P3rtnqWtC3yn/5T7r3fAbkYA4KfTU7cYwzVKEFNV/7o6/prQmwWEskpPKbzCloilFbsSThrP0s3IY37k5THNVYYppFw15ExrIhA+N3Xlqj6ITSpX1fIgCm0UpRl/23cQHeo96gi2YqRjOQoCHXfmyot74B5MBJSb+wCzrd2ZbnTrI=");
	
	String texture = "";
	String signature = "";
	String name = "";
	
	private NPCSkins(String name, String texture, String signature) {
		this.name = name;
		this.texture = texture;
		this.signature = signature;
	}
	
	public String getName() {
		return name;
	}
	public String getTexture() {
		return texture;
	}
	public String getSignature() {
		return signature;
	}
	
	public static NPCSkins getSkinByName(String name) {
		for(NPCSkins skin : NPCSkins.values()) {
			if(skin.getName().equals(name)) {
				return skin;
			}
		}
		return null;
	}
	
}
