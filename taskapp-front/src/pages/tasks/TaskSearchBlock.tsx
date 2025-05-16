import { Button, Paper, TextField} from "@mui/material";
import {useState} from "react";


export const TaskSearchBlock = ({searchHandle}: {searchHandle: (key:string) => void}) => {


    const [keyWord,setKeyWord] = useState('');

    const doSearch = () => {
        searchHandle(keyWord);
    }

    return (
            <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 1 }}>
                <TextField
                    fullWidth
                    value={keyWord}
                    onChange={(e) => setKeyWord(e.target.value)}
                    placeholder="Search..."
                />
                <Button variant="contained" onClick={doSearch}>
                    Search
                </Button>
            </Paper>
    );

};