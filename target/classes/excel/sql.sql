-- Create table
create table INSPECTION_OPINION_TEMPLATE
(
  ID                     VARCHAR2(50) not null,
  REPORT_NAME            VARCHAR2(50),
  STAGE_ORDER            VARCHAR2(100),
  FIELD_MAXLENGTH        VARCHAR2(20),
  FIELD_ID               VARCHAR2(50),
  FIELD_NAME             VARCHAR2(3000),
  FIELD_VALUE            VARCHAR2(3000),
  FIELD_CELLTYPE         VARCHAR2(50) default 'title',
  FIELD_CELLCONTENT      VARCHAR2(50),
  FIELD_RESULT           VARCHAR2(50),
  FIELD_COMMENT          VARCHAR2(3000),
  FIELD_ISSTRETCHCOLUMNS VARCHAR2(1),
  FIELD_WIDTH            NUMBER(4),
  FIELD_HEIGHT           NUMBER(4),
  FIELD_HEIGHTOFFSET     NUMBER(4),
  FIELD_ISCOLSPAN        VARCHAR2(1),
  FIELD_ISROWSPAN        VARCHAR2(1),
  FIELD_COLSPAN          NUMBER(4),
  FIELD_ROWSPAN          NUMBER(4),
  FIELD_ORDERCLSID       NUMBER(4),
  FIELD_ORDERROWID       NUMBER(4),
  UPDATE_DATE            DATE,
  PARENTID               VARCHAR2(20),
  PARENTORDERCLSID       VARCHAR2(4),
  PARENTORDERROWID       VARCHAR2(4),
  SE_TYPE                VARCHAR2(50),
  APPLY_TYPE             VARCHAR2(50),
  APPLY_KIND             VARCHAR2(50),
  INSPECT_TYPE_ID        VARCHAR2(50),
  GRAVITY                VARCHAR2(1),
  WIDTHNUM               VARCHAR2(20)
)
tablespace SUN
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 19M
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table INSPECTION_OPINION_TEMPLATE
  is '检验意见书布局模板';
-- Add comments to the columns 
comment on column INSPECTION_OPINION_TEMPLATE.ID
  is '主键';
comment on column INSPECTION_OPINION_TEMPLATE.REPORT_NAME
  is '报告名称';
comment on column INSPECTION_OPINION_TEMPLATE.STAGE_ORDER
  is '段落序号';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_MAXLENGTH
  is '段内序号';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_ID
  is '单元格id';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_NAME
  is '单元格名称';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_VALUE
  is '单元格的值';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_CELLTYPE
  is '字段类型title 标题，autotext 自动内容 ,text 内容， datetime 时间， radio 单选框， checkbox 复选款。';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_CELLCONTENT
  is '用于radio或者checkbox等类型的数据项展示';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_RESULT
  is '本单元格结果，1表示对号，0表示叉号';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_COMMENT
  is '标记字段的说明，用在可更改数据上';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_ISSTRETCHCOLUMNS
  is '本单元格是否自动填充扩容';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_WIDTH
  is '本单元格的宽度倍数';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_HEIGHT
  is '本单元格的高度倍数';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_HEIGHTOFFSET
  is '本单元格的高度偏移量';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_ISCOLSPAN
  is '本单元格是否为跨列';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_ISROWSPAN
  is '本单元格是否为跨行';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_COLSPAN
  is '占用列数';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_ROWSPAN
  is '占用行数';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_ORDERCLSID
  is '列序号';
comment on column INSPECTION_OPINION_TEMPLATE.FIELD_ORDERROWID
  is '行序号';
comment on column INSPECTION_OPINION_TEMPLATE.UPDATE_DATE
  is '更新日期';
comment on column INSPECTION_OPINION_TEMPLATE.PARENTID
  is '父框的id';
comment on column INSPECTION_OPINION_TEMPLATE.PARENTORDERCLSID
  is '父框列序号';
comment on column INSPECTION_OPINION_TEMPLATE.PARENTORDERROWID
  is '父框行序号';
comment on column INSPECTION_OPINION_TEMPLATE.SE_TYPE
  is '设备类别';
comment on column INSPECTION_OPINION_TEMPLATE.APPLY_TYPE
  is '检验类型';
comment on column INSPECTION_OPINION_TEMPLATE.APPLY_KIND
  is '检验种类';
comment on column INSPECTION_OPINION_TEMPLATE.INSPECT_TYPE_ID
  is '检验报告模板id，如000301001对应：定期检验，杂物电梯定期检验报告
';
comment on column INSPECTION_OPINION_TEMPLATE.GRAVITY
  is '对齐方式1左2中3右';
comment on column INSPECTION_OPINION_TEMPLATE.WIDTHNUM
  is '单元格宽度的具体数值没有的使用倍数（FIELD_WIDTH）设置';
