# Liquify

liquibase changeset을 sql로 변환하는 툴이고 [https://github.com/daquino/liquify](https://github.com/daquino/liquify) 를 가져와서 내부적으로 사용하기 위해 customize 했습니다.

### Usage
WEB-INF/classes/liquify_migration_to_query.xml 파일에 changeset을 넣고 run하면 동일 디렉토리에 sql파일이 생성됩니다.