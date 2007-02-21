package net.sf.cb2java.copybook.data;

public class Record extends GroupData
{
    public Record(String name, GroupData data)
    {
        super(data.definition, data.children);
    }
}