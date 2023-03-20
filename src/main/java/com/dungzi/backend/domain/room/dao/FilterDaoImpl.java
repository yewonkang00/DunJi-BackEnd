package com.dungzi.backend.domain.room.dao;

import com.dungzi.backend.domain.room.domain.RoomAddress;
import com.dungzi.backend.domain.room.dto.RoomRequestDto;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilterDaoImpl implements FilterDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<RoomAddress> findRoomByFilter(RoomRequestDto.FilterDto request) {
        String jpql = "SELECT a FROM RoomAddress a JOIN FETCH a.roomInfo i";
        String whereSql = " where ";
        List<String> whereCondition = new ArrayList<>();

        whereCondition.add("a.longitude > :startLongitude and a.latitude > :startLatitude");
        whereCondition.add("a.longitude < :endLongitude and a.latitude < :endLatitude");

        // 층 수 쿼리
        String floor = "";
        int floor_cnt = 0;

        if(request.isFloorFive()) {
            floor += "(i.floor <= 5";
            floor_cnt++;
        }
        if(request.isFloorSixUp()) {
            if(floor_cnt == 0)
                floor += "(";
            else
                floor += " or ";

            floor += "(i.floor >= 6 and i.floor <= 10)";
            floor_cnt++;
        }
        if(request.isFloorTenUp()) {
            if(floor_cnt == 0)
                floor += "(";
            else
                floor += " or ";

            floor += "i.floor >= 10";
            floor_cnt++;
        }
        if(request.isBasement()) {
            if(floor_cnt == 0)
                floor += "(";
            else
                floor += " or ";

            floor += "i.floor = '반지하'";
            floor_cnt++;
        }
        if(request.isRooftop()) {
            if(floor_cnt == 0)
                floor += "(";
            else
                floor += " or ";

            floor += "i.floor = '옥탑방'";
            floor_cnt++;
        }

        if(floor_cnt > 0)
            floor += ")";
        whereCondition.add(floor);

        // 거래 유형 쿼리
        if(request.getDealType() != null) {
            whereCondition.add("i.dealType = :dealType");
        }

        // 가격단위 쿼리
        if(request.getPriceUnit() != null) {
            whereCondition.add("i.priceUnit = :priceUnit");
        }

        // 방 종류 쿼리
        String room = "";
        int room_cnt = 0;
        if(request.isOneRoom()) {
            room += "(i.roomType like '원%'";
            room_cnt++;
        }
        if(request.isTwoRoom()) {
            if(room_cnt == 0)
                room += "(";
            else
                room += " or ";

            room += "i.roomType like '투%'";
            room_cnt++;
        }
        if(request.isThreeRoom()) {
            if(room_cnt == 0)
                room += "(";
            else
                room += " or ";

            room += "i.roomType like '쓰리%'";
            room_cnt++;
        }

        if(room_cnt > 0) {
            room += ")";
        }
        whereCondition.add(room);


        // 방 구조 쿼리
        String type = "";
        int type_cnt = 0;

        if(request.isOpenType()) {
            if(type_cnt == 0)
                type += "(";
            else
                type += " or ";

            type += "i.structure = '오픈형'";
            type_cnt++;
        }
        if(request.isSeparateType()) {
            if(type_cnt == 0)
                type += "(";
            else
                type += " or ";

            type += "i.structure = '분리형'";
            type_cnt++;
        }
        if(request.isTwoFloorType()) {
            if(type_cnt == 0)
                type += "(";
            else
                type += " or ";

            type += "i.structure = '복층형'";
            type_cnt++;
        }

        if(type_cnt > 0) {
            type += ")";
        }
        whereCondition.add(type);

        whereCondition.add("(i.price >= :priceFrom and i.price <= :priceTo)");                       // 가격 쿼리
        whereCondition.add("(i.managementCost >= :manageFrom and i.managementCost <= :manageTo)"); // 관리비 쿼리
        whereCondition.add("(i.roomSize >= :sizeFrom and i.roomSize <= :sizeTo)");                 // 방 크기 쿼리

        if(request.getPriceUnit() == "월")                                                    // 월세일 때 보증금 쿼리
            whereCondition.add("(i.deposit >= :depositFrom and i.deposit <= :depositTo)");

        
        // 추가 필터 쿼리
        if(request.isElevators())
            whereCondition.add("i.elevators = 1");
        if(request.isParking())
            whereCondition.add("i.parking = 1");
        if(request.isPets())
            whereCondition.add("i.pets = 1");
        if(request.isLoan())
            whereCondition.add("i.loan = 1");
        if(request.isWomenOnly())
            whereCondition.add("i.womenOnly = 1");
        if(request.isFullOption())
            whereCondition.add("i.fullOption = 1");

        jpql += whereSql;
        jpql += String.join(" and ", whereCondition);

        // 거래 가능만 보기
        jpql += " and a.status = 'active'";

        // 정렬 쿼리
        if(request.isPriceSort())
            jpql += " order by price asc";
//        if(request.isRecommendSort())
//            whereCondition.add(" order by recommend");

        TypedQuery<RoomAddress> query = em.createQuery(jpql, RoomAddress.class);

        query.setParameter("startLongitude", request.getStartLongitude());
        query.setParameter("startLatitude", request.getStartLatitude());
        query.setParameter("endLongitude", request.getEndLongitude());
        query.setParameter("endLatitude", request.getEndLatitude());

        if(request.getDealType() != null)
            query.setParameter("dealType", request.getDealType());
        if(request.getPriceUnit() != null)
            query.setParameter("priceUnit", request.getPriceUnit());

        query.setParameter("priceFrom", request.getPriceFrom());
        query.setParameter("priceTo", request.getPriceTo());
        query.setParameter("manageFrom", request.getManageFrom());
        query.setParameter("manageTo", request.getManageTo());
        query.setParameter("sizeFrom", request.getSizeFrom());
        query.setParameter("sizeTo", request.getSizeTo());

        if(request.getPriceUnit() == "월") {
            query.setParameter("depositFrom", request.getDepositFrom());
            query.setParameter("depositTo", request.getDepositTo());
        }

        return query.getResultList();
    }
}
