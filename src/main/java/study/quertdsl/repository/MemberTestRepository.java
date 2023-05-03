package study.quertdsl.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.quertdsl.dto.MemberSearchCondition;
import study.quertdsl.entity.Member;
import study.quertdsl.entity.QMember;
import study.quertdsl.repository.support.Querydsl4RepositorySupport;

import java.util.List;

import static study.quertdsl.entity.QMember.member;
import static study.quertdsl.entity.QTeam.team;

@Repository
public class MemberTestRepository extends Querydsl4RepositorySupport {
    public MemberTestRepository() {
        super(Member.class);
    }

    public List<Member> basicSelect(){
        return select(member)
                .from(member)
                .fetch();
    }

    public List<Member> basicSelectFrom(){
        return selectFrom(member)
                .fetch();
    }

    public Page<Member> searchPageByApplyPage(MemberSearchCondition condition, Pageable pageable){
        JPAQuery<Member> query = selectFrom(member)
                .leftJoin(member.team,team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGeo(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );

        JPAQuery<Long> countQuery = select(member.count())
                .from(member)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGeo(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );


        List<Member> content = getQuerydsl().applyPagination(pageable,query).fetch();
        //fetchCount @Deprecated
//        return PageableExecutionUtils.getPage(content, pageable, query::fetchCount);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Page<Member> searchPagenation(MemberSearchCondition condition, Pageable pageable){
        return applyPagination(pageable, query-> query
                        .selectFrom(member)
                        .leftJoin(member.team,team)
                        .where(usernameEq(condition.getUsername()),
                                teamNameEq(condition.getTeamName()),
                                ageGeo(condition.getAgeGoe()),
                                ageLoe(condition.getAgeLoe())
                        )
        );
    }

    public Page<Member> searchPagenation2(MemberSearchCondition condition, Pageable pageable){
        return applyPagination(pageable, contentQuery-> contentQuery
                .selectFrom(member)
                .leftJoin(member.team,team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGeo(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                ),countQuery -> countQuery
                .select(member.id)
                .from(member)
                .leftJoin(member.team,team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGeo(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))

        );
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGeo(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }
}
