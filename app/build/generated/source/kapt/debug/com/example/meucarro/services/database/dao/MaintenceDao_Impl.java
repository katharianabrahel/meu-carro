package com.example.meucarro.services.database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.meucarro.services.database.conversors.DateConverter;
import com.example.meucarro.services.database.models.Maintence;
import java.lang.Class;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MaintenceDao_Impl implements MaintenceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Maintence> __insertionAdapterOfMaintence;

  private final DateConverter __dateConverter = new DateConverter();

  private final EntityDeletionOrUpdateAdapter<Maintence> __deletionAdapterOfMaintence;

  private final EntityDeletionOrUpdateAdapter<Maintence> __updateAdapterOfMaintence;

  public MaintenceDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMaintence = new EntityInsertionAdapter<Maintence>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `maintences` (`id`,`remoteId`,`name`,`performedAt`,`nextDueAt`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Maintence value) {
        stmt.bindLong(1, value.getId());
        if (value.getRemoteId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getRemoteId());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        final Long _tmp = __dateConverter.fromDate(value.getPerformedAt());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        final Long _tmp_1 = __dateConverter.fromDate(value.getNextDueAt());
        if (_tmp_1 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp_1);
        }
        final Long _tmp_2 = __dateConverter.fromDate(value.getCreatedAt());
        if (_tmp_2 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp_2);
        }
        final Long _tmp_3 = __dateConverter.fromDate(value.getUpdatedAt());
        if (_tmp_3 == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, _tmp_3);
        }
      }
    };
    this.__deletionAdapterOfMaintence = new EntityDeletionOrUpdateAdapter<Maintence>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `maintences` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Maintence value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfMaintence = new EntityDeletionOrUpdateAdapter<Maintence>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `maintences` SET `id` = ?,`remoteId` = ?,`name` = ?,`performedAt` = ?,`nextDueAt` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Maintence value) {
        stmt.bindLong(1, value.getId());
        if (value.getRemoteId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getRemoteId());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        final Long _tmp = __dateConverter.fromDate(value.getPerformedAt());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        final Long _tmp_1 = __dateConverter.fromDate(value.getNextDueAt());
        if (_tmp_1 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp_1);
        }
        final Long _tmp_2 = __dateConverter.fromDate(value.getCreatedAt());
        if (_tmp_2 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp_2);
        }
        final Long _tmp_3 = __dateConverter.fromDate(value.getUpdatedAt());
        if (_tmp_3 == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, _tmp_3);
        }
        stmt.bindLong(8, value.getId());
      }
    };
  }

  @Override
  public Object insert(final Maintence maintence, final Continuation<? super Unit> $completion) {
    __db.assertNotSuspendingTransaction();
  }

  @Override
  public Object delete(final Maintence maintence, final Continuation<? super Unit> $completion) {
    __db.assertNotSuspendingTransaction();
  }

  @Override
  public Object update(final Maintence maintence, final Continuation<? super Unit> $completion) {
    __db.assertNotSuspendingTransaction();
  }

  @Override
  public Object getById(final int id, final Continuation<? super Maintence> $completion) {
    final String _sql = "SELECT * FROM maintences WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Object _result;
      if(_cursor.moveToFirst()) {
        _result = new Object();
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Object getAll(final Continuation<? super List<Maintence>> $completion) {
    final String _sql = "SELECT * FROM maintences";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    int _argIndex = 1;
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Object _result;
      if(_cursor.moveToFirst()) {
        _result = new Object();
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
