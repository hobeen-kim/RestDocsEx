package restapi.restdocs.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Authority implements EnumType{

    USER("일반 사용자"),
    ADMIN("관리자");

    private final String description;

    @Override
    public String getName() {
        return this.description;
    }

    @Override
    public String getDescription() {
        return this.name();
    }
}
