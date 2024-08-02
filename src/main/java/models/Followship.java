package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 日報データのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_FOL)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_FOL_COUNT_ALL_FEE,
            query = JpaConst.Q_FOL_COUNT_ALL_FEE_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_COUNT_ALL_FER,
            query = JpaConst.Q_FOL_COUNT_ALL_FER_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_GET_USERS_BY_FEE,
            query = JpaConst.Q_FOL_GET_USERS_BY_FEE_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_GET_USERS_BY_FER,
            query = JpaConst.Q_FOL_GET_USERS_BY_FER_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_COUNT_BY_FEE_AND_FER,
            query = JpaConst.Q_FOL_COUNT_BY_FEE_AND_FER_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_GET_BY_FEE_AND_FER,
            query = JpaConst.Q_FOL_GET_BY_FEE_AND_FER_DEF)
})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Followship {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.FOL_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * フォロイー
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.FOL_COL_FEE, nullable = false)
    private Employee followee;

    /**
     * フォロワー
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.FOL_COL_FER, nullable = false)
    private Employee follower;

    /**
     * 登録日時
     */
    @Column(name = JpaConst.FOL_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = JpaConst.FOL_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;

}